package com.example.dynasync.data.repository

import android.util.Log
import com.example.dynasync.data.dto.ProfileCreateDto
import com.example.dynasync.data.dto.UserMetaData
import com.example.dynasync.data.supabase.SupabaseClientObject
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

object RegisterRepository {
    val supabase = SupabaseClientObject.client

    suspend fun createUser(profileCreateDto: ProfileCreateDto, newImageBytes: ByteArray? = null) {
        var finalImageUrl: String? = null

        try {
            if (newImageBytes != null) {
                finalImageUrl = StorageHelper.uploadImage(
                    bucketName = "profile-images",
                    byteArray = newImageBytes,
                    fileNamePrefix = "prof-${profileCreateDto.email}"
                )
            }

            val metaData = UserMetaData(
                name = profileCreateDto.name,
                lastname = profileCreateDto.lastname,
                age = profileCreateDto.age,
                profileImageUrl = finalImageUrl
            )

            supabase.auth.signUpWith(Email) {
                this.email = profileCreateDto.email
                this.password = profileCreateDto.password
                this.data = Json.encodeToJsonElement(metaData).jsonObject
            }

            if (finalImageUrl != null) {
                try {
                    supabase.auth.updateUser {
                        data = Json.encodeToJsonElement(metaData).jsonObject
                    }
                } catch (e: Exception) {
                    Log.e(
                        "RegisterRepo",
                        "El usuario se creó pero falló la actualización extra: ${e.message}"
                    )
                }
            }

        } catch (e: Exception) {
            if (finalImageUrl != null) {
                try {
                    StorageHelper.deleteImage(
                        bucketName = "profile-images",
                        imageUrl = finalImageUrl
                    )
                } catch (rollbackError: Exception) {
                    Log.e("rollback", "Error rollback: $rollbackError")
                }
            }
            Log.e("debug", "Error al crear usuario: ${e}")
            throw e
        }
    }
}