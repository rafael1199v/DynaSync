package com.example.dynasync.data.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskUpdateDto(
    @SerialName("id")
    val id: Int? = null,

    @SerialName("title")
    val title: String,

    @SerialName("finish_date")
    val finishDate: LocalDate,

    @SerialName("personal_id")
    val personalId: Int?
)
