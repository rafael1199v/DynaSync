package com.example.dynasync.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${System.currentTimeMillis()}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
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