package com.pdfpptviewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
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
    private var currentPageIndex = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)
        
        pdfImageView = findViewById(R.id.pdfImageView)
        prevButton = findViewById(R.id.prevButton)
        nextButton = findViewById(R.id.nextButton)
        pageInfoText = findViewById(R.id.pageInfoText)
        rootLayout = findViewById(R.id.rootLayout)
        
        setTheme(if (isDarkMode()) R.style.Theme_PDFPPTViewer_Dark else R.style.Theme_PDFPPTViewer_Light)
        setupButtons()
        
        intent.data?.let { uri ->
            try {
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
                showPage(0)
                
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

    private fun isDarkMode(): Boolean {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        return sharedPref.getBoolean("dark_mode", false)
    }

    override fun onResume() {
        super.onResume()
        rootLayout.setBackgroundColor(
            ContextCompat.getColor(
                this,
                if (isDarkMode()) R.color.dark_background else R.color.light_background
            )
        )
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
            currentPage?.close()
            currentPage = renderer.openPage(index)
            currentPageIndex = index
            
            currentPage?.let { page ->
                val bitmap = Bitmap.createBitmap(
                    page.width * 2,
                    page.height * 2,
                    Bitmap.Config.ARGB_8888
                )
                
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                pdfImageView.setImageBitmap(bitmap)
                
                pageInfoText.text = getString(
                    R.string.page_info,
                    currentPageIndex + 1,
                    renderer.pageCount
                )
                
                prevButton.isEnabled = currentPageIndex > 0
                nextButton.isEnabled = currentPageIndex < renderer.pageCount - 1
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        currentPage?.close()
        pdfRenderer?.close()
    }
}
