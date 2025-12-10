package com.example.dynasync.domain

import kotlinx.datetime.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val personal: Personal,
    val finishDate: LocalDate
)