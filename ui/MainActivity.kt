package com.example.cloudstorage.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.cloudstorage.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainActivity : AppCompatActivity() {

    private val apiService = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uploadButton = findViewById<Button>(R.id.uploadButton)
        val downloadButton = findViewById<Button>(R.id.downloadButton)

        uploadButton.setOnClickListener { openFilePicker() }
        downloadButton.setOnClickListener { downloadFile() }
    }

    private fun openFilePicker() {
        val filePickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        filePickerLauncher.launch(filePickerIntent)
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val file = File(uri.path!!)
            uploadFile(file)
        }
    }

    private fun uploadFile(file: File) {
        GlobalScope.launch(Dispatchers.IO) {
            val requestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
            val ownerRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "owner")

            val response = apiService.uploadFile(filePart, ownerRequestBody)

            runOnUiThread {
                if (response.success) {
                    Toast.makeText(this@MainActivity, "Upload berhasil", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Upload gagal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun downloadFile() {
        GlobalScope.launch(Dispatchers.IO) {
            val owner = "owner"
            val response = apiService.downloadFile(owner)

            // Simpan file dan tampilkan pesan sukses
            runOnUiThread {
                Toast.makeText(this@MainActivity, "Download berhasil", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
