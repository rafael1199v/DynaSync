package com.example.dynasync.data.dto

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    @SerialName("name")
    val id: Int? = null,

    @SerialName("title")
    val title: String,

    @SerialName("objective")
    val objective: String,

    @SerialName("description")
    val description: String,

    @SerialName("finish_date")
    val finishDate: LocalDate? = null,

    @SerialName("image_url")
    val imageUrl: String? = null,

    @SerialName("created_at")
    val createdAt: String? = null
)
