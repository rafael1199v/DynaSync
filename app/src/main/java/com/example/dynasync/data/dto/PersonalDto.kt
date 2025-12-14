package com.example.dynasync.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PersonalDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("name")
    val name: String,

    @SerialName("lastname")
    val lastname: String,

    @SerialName("charge")
    val charge: String,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)