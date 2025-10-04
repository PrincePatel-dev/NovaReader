package com.pdfpptviewer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.material.card.MaterialCardView
import java.io.File
import java.io.FileOutputStream

class PdfViewerActivity : AppCompatActivity() {
    private lateinit var pdfView: PDFView
    private lateinit var pageInfoText: TextView
    private lateinit var pageInfoContainer: MaterialCardView
    private lateinit var rootLayout: ConstraintLayout

    private var tempFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        pdfView = findViewById(R.id.pdfView)
        pageInfoText = findViewById(R.id.pageInfoText)
        pageInfoContainer = findViewById(R.id.pageInfoContainer)
        rootLayout = findViewById(R.id.rootLayout)
        pageInfoContainer.visibility = View.GONE

        applyThemeColors()

        intent.data?.let { uri ->
            try {
                val inputStream = contentResolver.openInputStream(uri)
                tempFile = File.createTempFile("temp_pdf", ".pdf", cacheDir)

                FileOutputStream(tempFile).use { output ->
                    inputStream?.copyTo(output)
                }

                tempFile?.let { displayPdf(it) }
            } catch (e: Exception) {
                Log.e("PdfViewer", "Failed to open PDF", e)
                Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show()
                finish()
            }
        } ?: run {
            Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun applyThemeColors() {
        val darkMode = isDarkModeEnabled()
        val backgroundColor = ContextCompat.getColor(
            this,
            if (darkMode) R.color.dark_background else R.color.light_background
        )
        val textColor = ContextCompat.getColor(
            this,
            if (darkMode) R.color.dark_text else R.color.light_text
        )

        rootLayout.setBackgroundColor(backgroundColor)
        pageInfoContainer.setCardBackgroundColor(backgroundColor)
        pageInfoText.setTextColor(textColor)
        pdfView.setBackgroundColor(backgroundColor)
    }

    private fun displayPdf(file: File) {
        try {
            pdfView.fromFile(file)
                .pageFitPolicy(FitPolicy.WIDTH)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .autoSpacing(true)
                .pageSnap(true)
                .pageFling(true)
                .enableDoubletap(true)
                .nightMode(isDarkModeEnabled())
                .scrollHandle(DefaultScrollHandle(this))
                .onPageChange { page, pageCount ->
                    updatePageInfo(page, pageCount)
                }
                .onError { throwable ->
                    Log.e("PdfViewer", "Error rendering PDF", throwable)
                    Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show()
                    finish()
                }
                .load()
        } catch (e: Exception) {
            Log.e("PdfViewer", "Unexpected error while displaying PDF", e)
            Toast.makeText(this, R.string.error_opening_file, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updatePageInfo(pageIndex: Int, pageCount: Int) {
        pageInfoText.text = getString(R.string.page_info, pageIndex + 1, pageCount)
        pageInfoContainer.visibility = View.VISIBLE
    }

    private fun isDarkModeEnabled(): Boolean {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        return sharedPref.getBoolean("dark_mode", false)
    }

    override fun onDestroy() {
        try {
            pdfView.recycle()
            tempFile?.delete()
        } catch (e: Exception) {
            Log.w("PdfViewer", "Error cleaning up PDF resources", e)
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
