package com.pdfpptviewer

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.xslf.usermodel.XMLSlideShow
import org.apache.poi.hslf.usermodel.HSLFSlideShow
import java.io.ByteArrayInputStream

class PptViewerActivity : AppCompatActivity() {
    
    private lateinit var slideContentText: TextView
    private lateinit var prevButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var slideInfoText: TextView
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var loadingIndicator: ProgressBar
    
    private var slides = mutableListOf<String>()
    private var currentSlideIndex = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ppt_viewer)
        
        slideContentText = findViewById(R.id.slideContentText)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        slideInfoText = findViewById(R.id.slideInfoText)
        rootLayout = findViewById(R.id.rootLayout)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        
        applyThemeColors()
        setupButtons()
        
        intent.data?.let { uri ->
            loadingIndicator.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val mimeType = contentResolver.getType(uri)
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        val fileBytes = inputStream.readBytes()
                        loadPresentation(fileBytes, mimeType)
                    }
                    loadingIndicator.visibility = View.GONE
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadingIndicator.visibility = View.GONE
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
    
    private fun applyThemeColors() {
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        
        if (isDarkMode) {
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_background))
            slideContentText.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
            slideInfoText.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
        } else {
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.light_background))
            slideContentText.setTextColor(ContextCompat.getColor(this, R.color.light_text))
            slideInfoText.setTextColor(ContextCompat.getColor(this, R.color.light_text))
        }
    }
    
    private suspend fun loadPresentation(fileBytes: ByteArray, mimeType: String?) {
        withContext(Dispatchers.IO) {
            try {
                val isPptx = mimeType == "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                
                if (isPptx) {
                    ByteArrayInputStream(fileBytes).use { stream ->
                        val ppt = XMLSlideShow(stream)
                        ppt.slides.forEach { slide ->
                            val slideText = StringBuilder()
                            slide.shapes.forEach { shape ->
                                if (shape is org.apache.poi.xslf.usermodel.XSLFTextShape) {
                                    slideText.append(shape.text).append("\n\n")
                                }
                            }
                            slides.add(slideText.toString().trim())
                        }
                        ppt.close()
                    }
                } else {
                    try {
                        ByteArrayInputStream(fileBytes).use { stream ->
                            val ppt = XMLSlideShow(stream)
                            ppt.slides.forEach { slide ->
                                val slideText = StringBuilder()
                                slide.shapes.forEach { shape ->
                                    if (shape is org.apache.poi.xslf.usermodel.XSLFTextShape) {
                                        slideText.append(shape.text).append("\n\n")
                                    }
                                }
                                slides.add(slideText.toString().trim())
                            }
                            ppt.close()
                        }
                    } catch (xmlEx: Exception) {
                        ByteArrayInputStream(fileBytes).use { stream ->
                            val ppt = HSLFSlideShow(stream)
                            ppt.slides.forEach { slide ->
                                val slideText = StringBuilder()
                                slide.shapes.forEach { shape ->
                                    if (shape is org.apache.poi.hslf.usermodel.HSLFTextShape) {
                                        slideText.append(shape.text).append("\n\n")
                                    }
                                }
                                slides.add(slideText.toString().trim())
                            }
                            ppt.close()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PptViewerActivity,
                        R.string.error_opening_file,
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                return@withContext
            }
        }
        
        withContext(Dispatchers.Main) {
            if (slides.isNotEmpty()) {
                showSlide(0)
            } else {
                Toast.makeText(
                    this@PptViewerActivity,
                    "No slides found in presentation",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
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
    }
    
    private fun showSlide(index: Int) {
        if (index >= 0 && index < slides.size) {
            currentSlideIndex = index
            
            // Format slide content with better spacing and styling
            val formattedText = formatSlideContent(slides[index])
            slideContentText.text = formattedText
            
            slideInfoText.text = getString(
                R.string.slide_info,
                currentSlideIndex + 1,
                slides.size
            )
            
            prevButton.isEnabled = currentSlideIndex > 0
            nextButton.isEnabled = currentSlideIndex < slides.size - 1
        }
    }
    
    private fun formatSlideContent(content: String): String {
        // Add extra spacing between paragraphs for better readability
        return content.replace("\n\n", "\n\n\n")
            .trim()
    }
}
