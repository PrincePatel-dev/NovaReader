package com.pdfpptviewer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    
    private lateinit var darkModeSwitch: SwitchMaterial
    private lateinit var openPdfButton: MaterialButton
    private lateinit var openPptButton: MaterialButton
    private lateinit var statusText: TextView
    
    private var pendingFileType: String? = null
    
    private val pdfFilePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { openPdfViewer(it) }
    }
    
    private val pptFilePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { openPptViewer(it) }
    }
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        openPdfButton = findViewById(R.id.openPdfButton)
        openPptButton = findViewById(R.id.openPptButton)
        statusText = findViewById(R.id.statusText)
        
        setupDarkModeSwitch()
        setupButtons()
    }
    
    private fun setupDarkModeSwitch() {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        
        darkModeSwitch.isChecked = isDarkMode
        
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean("dark_mode", isChecked)
            editor.apply()
            
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    
    private fun setupButtons() {
        openPdfButton.setOnClickListener {
            launchFilePicker("pdf")
        }
        
        openPptButton.setOnClickListener {
            launchFilePicker("ppt")
        }
    }
    
    private fun launchFilePicker(fileType: String) {
        when (fileType) {
            "pdf" -> {
                pdfFilePickerLauncher.launch(arrayOf("application/pdf"))
            }
            "ppt" -> {
                pptFilePickerLauncher.launch(
                    arrayOf(
                        "application/vnd.ms-powerpoint",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                    )
                )
            }
        }
    }
    
    private fun openPdfViewer(uri: Uri) {
        val intent = Intent(this, PdfViewerActivity::class.java).apply {
            data = uri
        }
        startActivity(intent)
    }
    
    private fun openPptViewer(uri: Uri) {
        val intent = Intent(this, PptViewerActivity::class.java).apply {
            data = uri
        }
        startActivity(intent)
    }
}
