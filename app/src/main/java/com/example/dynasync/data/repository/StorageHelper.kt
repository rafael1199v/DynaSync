package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.supabase.SupabaseClientObject
import io.github.jan.supabase.storage.storage
import kotlin.time.Clock

object StorageHelper {
    private val storage = SupabaseClientObject.client.storage

    suspend fun uploadImage(bucketName: String, byteArray: ByteArray, fileNamePrefix: String): String {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val fileName = "$fileNamePrefix-$timestamp.jpg"

        val bucket = storage.from(bucketName)
        bucket.upload(fileName, byteArray) {
            upsert = true
        }

        return bucket.publicUrl(fileName)
    }

    suspend fun deleteImage(bucketName: String, imageUrl: String) {
        try {
            val fileName = imageUrl.substringAfterLast("/")
            storage.from(bucketName).delete(listOf(fileName))
        } catch (e: Exception) {
            Log.e("debug","No se pudo borrar la imagen anterior: ${e.message}")
        }
    }
}