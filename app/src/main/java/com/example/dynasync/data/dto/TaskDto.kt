package com.example.dynasync.data.dto

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("title")
    val title: String,

    @SerialName("is_completed")
    val isCompleted: Boolean,

    @SerialName("finish_date")
    val finishDate: LocalDate,

    @SerialName("project_id")
    val projectId: Int,

    @SerialName("personal_id")
    val personalId: Int? = null,

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("personal")
    val personal: PersonalDto? = null
)