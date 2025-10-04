package com.pdfpptviewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.ImageView
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
import java.io.File
import java.io.FileOutputStream

class PdfViewerActivity : AppCompatActivity() {
    
    private lateinit var pdfImageView: ImageView
    private lateinit var prevButton: MaterialButton
    private lateinit var nextButton: MaterialButton
    private lateinit var pageInfoText: TextView
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var loadingIndicator: ProgressBar
    
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var currentPageIndex = 0
    
    // Cache for rendered pages to improve performance
    private val pageCache = mutableMapOf<Int, Bitmap>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        
        pdfImageView = findViewById(R.id.pdfImageView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        pageInfoText = findViewById(R.id.pageInfoText)
        rootLayout = findViewById(R.id.rootLayout)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        
        applyThemeColors()
        setupButtons()
        
        intent.data?.let { uri ->
            loadingIndicator.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    withContext(Dispatchers.IO) {
                        val inputStream = contentResolver.openInputStream(uri)
                        val tempFile = File.createTempFile("temp_pdf", ".pdf", cacheDir)
                        
                        FileOutputStream(tempFile).use { output ->
                            inputStream?.copyTo(output)
                        }
                        
                        val fileDescriptor = ParcelFileDescriptor.open(
                            tempFile,
                            ParcelFileDescriptor.MODE_READ_ONLY
                        )
                        
                        pdfRenderer = PdfRenderer(fileDescriptor)
                    }
                    
                    loadingIndicator.visibility = View.GONE
                    showPage(0)
                    
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadingIndicator.visibility = View.GONE
                    Toast.makeText(this@PdfViewerActivity, R.string.error_opening_file, Toast.LENGTH_LONG).show()
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
            pageInfoText.setTextColor(ContextCompat.getColor(this, R.color.dark_text))
        } else {
            rootLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.light_background))
            pageInfoText.setTextColor(ContextCompat.getColor(this, R.color.light_text))
        }
    }
    
    private fun setupButtons() {
        prevButton.setOnClickListener {
            if (currentPageIndex > 0) {
                showPage(currentPageIndex - 1)
            }
        }
        
        nextButton.setOnClickListener {
            pdfRenderer?.let { renderer ->
                if (currentPageIndex < renderer.pageCount - 1) {
                    showPage(currentPageIndex + 1)
                }
            }
        }
    }
    
    private fun showPage(index: Int) {
        pdfRenderer?.let { renderer ->
            currentPageIndex = index
            
            // Check if page is already cached
            val cachedBitmap = pageCache[index]
            if (cachedBitmap != null) {
                pdfImageView.setImageBitmap(cachedBitmap)
                updatePageInfo(renderer.pageCount)
                return
            }
            
            // Render page in background thread for smooth performance
            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO) {
                    currentPage?.close()
                    currentPage = renderer.openPage(index)
                    
                    currentPage?.let { page ->
                        // Use higher resolution for better quality
                        val bitmap = Bitmap.createBitmap(
                            page.width * 3,
                            page.height * 3,
                            Bitmap.Config.ARGB_8888
                        )
                        
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        
                        // Cache the rendered page
                        if (pageCache.size < 5) { // Keep last 5 pages cached
                            pageCache[index] = bitmap
                        }
                        
                        bitmap
                    }
                }
                
                bitmap?.let {
                    pdfImageView.setImageBitmap(it)
                }
                
                updatePageInfo(renderer.pageCount)
            }
        }
    }
    
    private fun updatePageInfo(totalPages: Int) {
        pageInfoText.text = getString(
            R.string.page_info,
            currentPageIndex + 1,
            totalPages
        )
        
        prevButton.isEnabled = currentPageIndex > 0
        nextButton.isEnabled = currentPageIndex < totalPages - 1
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clear cached bitmaps
        pageCache.values.forEach { it.recycle() }
        pageCache.clear()
        
        currentPage?.close()
        pdfRenderer?.close()
    }
}
