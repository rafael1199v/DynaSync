package com.example.dynasync.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileCreateDto(
    val email: String,
    val name: String,
    val lastname: String,
    val age: Int,
    val imageUrl: String?,
    val password: String
)

@Serializable
data class UserMetaData(
    @SerialName("name")
    val name: String,

    @SerialName("last_name")
    val lastname: String,

    @SerialName("age")
    val age: Int,

    @SerialName("profile_image_url")
    val profileImageUrl: String?
)
