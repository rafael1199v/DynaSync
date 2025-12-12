package com.example.dynasync.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

fun Context.uriToFile(uri: Uri): File? {
    return try {
        val contentResolver = contentResolver
        val myFile = createImageFile()
        val inputStream = contentResolver.openInputStream(uri) as? InputStream
        val outputStream = FileOutputStream(myFile)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        myFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}