package com.example.dynasync.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant


@Serializable
data class ProfileDto(

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("age")
    val age: Int,

    @SerialName("profile_image_url")
    val profileImageUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)
