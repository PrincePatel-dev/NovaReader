package com.pdfpptviewer

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.hslf.usermodel.*
import org.apache.poi.xslf.usermodel.*
import java.io.ByteArrayInputStream

class PptViewerActivity : AppCompatActivity() {
    
    private lateinit var slideContentText: TextView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var slideInfoText: TextView
    private lateinit var rootLayout: ConstraintLayout
    
    private var slides = mutableListOf<String>()
    private var currentSlideIndex = 0
    private var hasShownFormattingNotice = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(if (isDarkMode()) R.style.Theme_PDFPPTViewer_Dark else R.style.Theme_PDFPPTViewer_Light)
        setContentView(R.layout.activity_ppt_viewer)
        
        slideContentText = findViewById(R.id.slideContentText)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        slideInfoText = findViewById(R.id.slideInfoText)
        rootLayout = findViewById(R.id.rootLayout)
        
        setupButtons()
        applyThemeToContent()
        
        intent.data?.let { uri ->
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val mimeType = contentResolver.getType(uri)
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        val fileBytes = inputStream.readBytes()
                        loadPresentation(fileBytes, mimeType)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@PptViewerActivity,
                        R.string.error_opening_file,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        } ?: run {
            Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    
    private fun isDarkMode(): Boolean {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        return sharedPref.getBoolean("dark_mode", false)
    }
    
    private fun applyThemeToContent() {
        rootLayout.setBackgroundColor(
            ContextCompat.getColor(
                this,
                if (isDarkMode()) R.color.dark_background else R.color.light_background
            )
        )
        
        slideContentText.setTextColor(
            ContextCompat.getColor(
                this,
                if (isDarkMode()) R.color.dark_text else R.color.light_text
            )
        )
        
        slideInfoText.setTextColor(
            ContextCompat.getColor(
                this,
                if (isDarkMode()) R.color.dark_text else R.color.light_text
            )
        )
    }
    
    private suspend fun loadPresentation(fileBytes: ByteArray, mimeType: String?) {
        withContext(Dispatchers.IO) {
            try {
                slides.clear()
                Log.d("PptViewer", "Loading presentation: mimeType=$mimeType, size=${fileBytes.size} bytes")

                var extractedSlides: List<String>? = null

                // First try modern PPTX format
                try {
                    ByteArrayInputStream(fileBytes).use { stream ->
                        val ppt = XMLSlideShow(stream)
                        Log.d("PptViewer", "Successfully loaded PPTX with ${ppt.slides.size} slides")

                        if (ppt.slides.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@PptViewerActivity,
                                    R.string.no_slides_found,
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            }
                            ppt.close()
                            return@withContext
                        }

                        extractedSlides = extractSlidesFromXml(ppt)
                        ppt.close()
                    }
                } catch (e: Exception) {
                    Log.w("PptViewer", "Failed to load as PPTX: ${e.message}")
                }

                // If PPTX failed, try legacy PPT format
                if (extractedSlides == null) {
                    try {
                        ByteArrayInputStream(fileBytes).use { stream ->
                            val ppt = HSLFSlideShow(stream)
                            Log.d("PptViewer", "Successfully loaded PPT with ${ppt.slides.size} slides")

                            if (ppt.slides.isEmpty()) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        this@PptViewerActivity,
                                        R.string.no_slides_found,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                }
                                ppt.close()
                                return@withContext
                            }

                            extractedSlides = extractSlidesFromHslf(ppt)
                            ppt.close()
                        }
                    } catch (e: Exception) {
                        Log.e("PptViewer", "Failed to load as PPT: ${e.message}")
                    }
                }

                if (extractedSlides == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PptViewerActivity,
                            R.string.unsupported_ppt_format,
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                    return@withContext
                }

                slides.clear()
                slides.addAll(extractedSlides!!)

                withContext(Dispatchers.Main) {
                    currentSlideIndex = 0
                    showSlide(0)
                    showFormattingNoticeIfNeeded()
                }
            } catch (e: Exception) {
                Log.e("PptViewer", "Error loading presentation", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PptViewerActivity,
                        R.string.error_opening_file,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun extractSlidesFromXml(ppt: XMLSlideShow): List<String> {
        val extractedSlides = mutableListOf<String>()
        ppt.slides.forEachIndexed { index, slide ->
            val builder = StringBuilder()
            slide.shapes.forEach { shape ->
                runCatching { appendXslfShapeText(shape, builder) }
                    .onFailure { Log.w("PptViewer", "Skipping PPTX shape on slide ${index + 1}: ${it.message}") }
            }
            extractedSlides.add(finalizeSlideText(builder, index))
        }
        return extractedSlides
    }

    private fun extractSlidesFromHslf(ppt: HSLFSlideShow): List<String> {
        val extractedSlides = mutableListOf<String>()
        ppt.slides.forEachIndexed { index, slide ->
            val builder = StringBuilder()
            slide.shapes.forEach { shape ->
                runCatching { appendHslfShapeText(shape, builder) }
                    .onFailure { Log.w("PptViewer", "Skipping PPT shape on slide ${index + 1}: ${it.message}") }
            }
            extractedSlides.add(finalizeSlideText(builder, index))
        }
        return extractedSlides
    }

    private fun appendXslfShapeText(shape: XSLFShape, builder: StringBuilder) {
        when (shape) {
            is XSLFTextShape -> appendXslfTextShape(shape, builder)
            is XSLFGroupShape -> shape.shapes.forEach { appendXslfShapeText(it, builder) }
            is XSLFTable -> shape.rows.forEachIndexed { rowIndex: Int, row: XSLFTableRow ->
                row.cells.forEachIndexed { cellIndex: Int, cell: XSLFTableCell ->
                    runCatching { appendXslfTextShape(cell, builder) }
                        .onFailure {
                            Log.w(
                                "PptViewer",
                                "Skipping PPTX table cell [$rowIndex,$cellIndex]: ${it.message}"
                            )
                        }
                }
            }
        }
    }

    private fun appendHslfShapeText(shape: HSLFShape, builder: StringBuilder) {
        when (shape) {
            is HSLFTextShape -> appendHslfTextShape(shape, builder)
            is HSLFGroupShape -> shape.shapes.forEach { appendHslfShapeText(it, builder) }
            is HSLFTable -> {
                for (row in 0 until shape.numberOfRows) {
                    for (col in 0 until shape.numberOfColumns) {
                        val cell = shape.getCell(row, col)
                        runCatching { appendHslfTextShape(cell, builder) }
                            .onFailure {
                                Log.w(
                                    "PptViewer",
                                    "Skipping PPT table cell [$row,$col]: ${it.message}"
                                )
                            }
                    }
                }
            }
        }
    }

    private fun appendXslfTextShape(shape: XSLFTextShape, builder: StringBuilder) {
        shape.textParagraphs.forEach { paragraph ->
            val paragraphText = paragraph.textRuns.joinToString(separator = "") { run ->
                run.rawText?.replace("\r", "") ?: ""
            }.trim()

            appendParagraph(builder, paragraphText, runCatching { paragraph.isBullet }.getOrDefault(false))
        }
    }

    private fun appendHslfTextShape(shape: HSLFTextShape?, builder: StringBuilder) {
        if (shape == null) return
        shape.textParagraphs.forEach { paragraph ->
            val paragraphText = paragraph.textRuns.joinToString(separator = "") { run ->
                (run.rawText ?: "").replace("\r", "")
            }.trim()

            appendParagraph(builder, paragraphText, runCatching { paragraph.isBullet }.getOrDefault(false))
        }
    }

    private fun appendParagraph(builder: StringBuilder, text: String, isBullet: Boolean) {
        if (text.isBlank()) return

        if (builder.isNotEmpty()) {
            builder.append('\n')
        }

        if (isBullet && !text.startsWith("•")) {
            builder.append("• ")
        }

        builder.append(text.trim())
    }

    private fun finalizeSlideText(builder: StringBuilder, index: Int): String {
        val content = builder.toString().trim()
        return if (content.isNotEmpty()) {
            content
        } else {
            getString(R.string.slide_images_only_placeholder, index + 1)
        }
    }

    private fun showFormattingNoticeIfNeeded() {
        if (!hasShownFormattingNotice) {
            Toast.makeText(this, R.string.ppt_text_only_notice, Toast.LENGTH_LONG).show()
            hasShownFormattingNotice = true
        }
    }
    
    private fun setupButtons() {
        prevButton.setOnClickListener {
            if (currentSlideIndex > 0) {
                showSlide(currentSlideIndex - 1)
            }
        }
        
        nextButton.setOnClickListener {
            if (currentSlideIndex < slides.size - 1) {
                showSlide(currentSlideIndex + 1)
            }
        }
        
        updateButtonStates()
    }
    
    private fun updateButtonStates() {
        prevButton.isEnabled = currentSlideIndex > 0
        nextButton.isEnabled = currentSlideIndex < slides.size - 1
        
        prevButton.alpha = if (prevButton.isEnabled) 1.0f else 0.5f
        nextButton.alpha = if (nextButton.isEnabled) 1.0f else 0.5f
    }
    
    private fun showSlide(index: Int) {
        if (index >= 0 && index < slides.size) {
            currentSlideIndex = index
            
            val slideContent = slides[index]
            val formattedContent = slideContent
                .replace("\\n", "\n")
                .replace("  ", " ")
                .trim()
            
            slideContentText.text = if (formattedContent.isNotEmpty()) {
                formattedContent
            } else {
                "Slide content could not be displayed"
            }
            
            slideInfoText.text = getString(
                R.string.slide_info,
                currentSlideIndex + 1,
                slides.size
            )
            
            updateButtonStates()
        }
    }
    
    override fun onBackPressed() {
        try {
            super.onBackPressed()
        } catch (e: Exception) {
            finish()
        }
    }
}