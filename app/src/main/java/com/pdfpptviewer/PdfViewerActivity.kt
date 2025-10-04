package com.pdfpptviewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream

class PdfViewerActivity : AppCompatActivity() {
    
    private lateinit var pdfImageView: ImageView
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private lateinit var pageInfoText: TextView
    private lateinit var rootLayout: ConstraintLayout
    
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var currentBitmap: Bitmap? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null
    private var currentPageIndex = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        
        pdfImageView = findViewById(R.id.pdfImageView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        pageInfoText = findViewById(R.id.pageInfoText)
        rootLayout = findViewById(R.id.rootLayout)
        
        applyThemeColors()
        setupButtons()
        
        intent.data?.let { uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val tempFile = File.createTempFile("temp_pdf", ".pdf", cacheDir)
                
                FileOutputStream(tempFile).use { output ->
                    inputStream?.copyTo(output)
                }
                
                parcelFileDescriptor = ParcelFileDescriptor.open(
                    tempFile,
                    ParcelFileDescriptor.MODE_READ_ONLY
                )

                parcelFileDescriptor?.let { descriptor ->
                    pdfRenderer = PdfRenderer(descriptor)
                    showPage(0)
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show()
                finish()
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
        
        // Update button states
        updateButtonStates()
    }
    
    private fun updateButtonStates() {
        pdfRenderer?.let { renderer ->
            prevButton.isEnabled = currentPageIndex > 0
            nextButton.isEnabled = currentPageIndex < renderer.pageCount - 1
            
            prevButton.alpha = if (prevButton.isEnabled) 1.0f else 0.5f
            nextButton.alpha = if (nextButton.isEnabled) 1.0f else 0.5f
        }
    }
    
    private fun showPage(index: Int) {
        pdfRenderer?.let { renderer ->
            try {
                val previousBitmap = currentBitmap

                // Clean up previous page first
                currentPage?.close()
                currentPage = null

                currentPage = renderer.openPage(index)

                currentPage?.let { page ->
                    // Optimized rendering - balance quality with memory usage
                    val displayMetrics = resources.displayMetrics
                    val scaleFactor = when {
                        renderer.pageCount > 25 -> 1.0f
                        renderer.pageCount > 10 -> 1.2f
                        else -> 1.4f
                    }
                    val width = (page.width * displayMetrics.density * scaleFactor).toInt().coerceAtLeast(1)
                    val height = (page.height * displayMetrics.density * scaleFactor).toInt().coerceAtLeast(1)

                    val safeBitmap = createAndRenderBitmap(page, width, height)
                    safeBitmap?.let { renderedBitmap ->
                        val displayBitmap = if (isDarkMode()) {
                            val inverted = invertBitmapColors(renderedBitmap)
                            renderedBitmap.recycle()
                            inverted
                        } else {
                            renderedBitmap
                        }

                        currentBitmap = displayBitmap
                        pdfImageView.setImageBitmap(displayBitmap)
                        currentPageIndex = index

                        previousBitmap?.let { oldBitmap ->
                            if (oldBitmap != currentBitmap && !oldBitmap.isRecycled) {
                                oldBitmap.recycle()
                            }
                        }

                        pageInfoText.text = getString(
                            R.string.page_info,
                            currentPageIndex + 1,
                            renderer.pageCount
                        )

                        updateButtonStates()
                    } ?: run {
                        page.close()
                        currentPage = null
                        runOnUiThread {
                            Toast.makeText(
                                this@PdfViewerActivity,
                                "Unable to render page due to low memory.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PdfViewer", "Error showing page $index", e)
                // Show error message to user
                runOnUiThread {
                    Toast.makeText(this@PdfViewerActivity, "Error loading page. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    private fun createAndRenderBitmap(page: PdfRenderer.Page, width: Int, height: Int): Bitmap? {
        return try {
            val maxMemory = Runtime.getRuntime().maxMemory()
            val usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
            val freeMemory = maxMemory - usedMemory
            val requiredMemory = width.toLong() * height.toLong() * 4L

            if (requiredMemory > freeMemory * 0.85) {
                if (width <= 1 || height <= 1) {
                    Log.w("PdfViewer", "Insufficient memory to render even minimal bitmap")
                    runOnUiThread {
                        Toast.makeText(this, "Not enough memory to render page.", Toast.LENGTH_LONG).show()
                    }
                    return null
                }
                val scaledWidth = (width * 0.7f).toInt().coerceAtLeast(1)
                val scaledHeight = (height * 0.7f).toInt().coerceAtLeast(1)
                return createAndRenderBitmap(page, scaledWidth, scaledHeight)
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(
                if (isDarkMode()) android.graphics.Color.rgb(30, 30, 30)
                else android.graphics.Color.WHITE
            )

            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            bitmap
        } catch (e: OutOfMemoryError) {
            Log.e("PdfViewer", "Out of memory rendering page", e)
            runOnUiThread {
                Toast.makeText(this, "Memory error. Try closing other apps.", Toast.LENGTH_LONG).show()
            }
            null
        } catch (e: Exception) {
            Log.e("PdfViewer", "Error creating bitmap", e)
            runOnUiThread {
                Toast.makeText(this, "Error rendering page.", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    private fun clearCurrentBitmap() {
        currentBitmap?.let { bitmap ->
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
        currentBitmap = null
    }

    private fun isDarkMode(): Boolean {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        return sharedPref.getBoolean("dark_mode", false)
    }
    
    private fun invertBitmapColors(original: Bitmap): Bitmap {
        val invertedBitmap = Bitmap.createBitmap(original.width, original.height, original.config)
        val canvas = android.graphics.Canvas(invertedBitmap)
        val paint = android.graphics.Paint()
        
        // Color matrix to invert colors (makes dark text light and light backgrounds dark)
        val colorMatrix = android.graphics.ColorMatrix(floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,  // Red
            0f, -1f, 0f, 0f, 255f,  // Green  
            0f, 0f, -1f, 0f, 255f,  // Blue
            0f, 0f, 0f, 1f, 0f      // Alpha
        ))
        
        paint.colorFilter = android.graphics.ColorMatrixColorFilter(colorMatrix)
        canvas.drawBitmap(original, 0f, 0f, paint)
        
        return invertedBitmap
    }
    
    override fun onDestroy() {
        clearCurrentBitmap()
        try {
            currentPage?.close()
            pdfRenderer?.close()
            parcelFileDescriptor?.close()
        } catch (e: Exception) {
            Log.e("PdfViewer", "Error releasing PDF resources", e)
        }
        super.onDestroy()
    }
    
    override fun onBackPressed() {
        try {
            super.onBackPressed()
        } catch (e: Exception) {
            finish()
        }
    }
}
