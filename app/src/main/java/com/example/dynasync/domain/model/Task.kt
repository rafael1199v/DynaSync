package com.example.dynasync.domain.model

import kotlinx.datetime.LocalDate
import kotlin.time.Instant


data class Task(
    val id: Int,
    val title: String,
    //val description: String,
    val isCompleted: Boolean,
    val personal: Personal?,
    val finishDate: LocalDate,
    val createdAt: Instant? = null
)