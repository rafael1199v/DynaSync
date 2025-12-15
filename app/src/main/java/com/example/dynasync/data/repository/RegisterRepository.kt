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

            Log.d("testing", "createUser: $finalImageUrl")


            val metaData = UserMetaData(
                name = profileCreateDto.name,
                lastname = profileCreateDto.lastname,
                age = profileCreateDto.age,
                profileImageUrl = finalImageUrl
            )

            Log.d("testing", "createUser: $metaData")

            supabase.auth.signUpWith(Email) {
                this.email = profileCreateDto.email
                this.password = profileCreateDto.password

                this.data =  Json.encodeToJsonElement(metaData).jsonObject
            }

            Log.d("testing", "se supone que termino")


        }
        catch (e: Exception) {
            if (finalImageUrl != null) {
                try {
                    StorageHelper.deleteImage(
                        bucketName = "profile-images",
                        imageUrl = finalImageUrl
                    )
                } catch (rollbackError: Exception) {
                    Log.e(
                        "rollback",
                        "No se pudo eliminar la imagen durante rollback: $rollbackError"
                    )
                }
            }


            Log.e("debug", "Hubo un error al crear el usuario. ${e}")
            throw e
        }

    }
}