package com.example.dynasync.data

import kotlinx.datetime.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val personInChargeId: Int,
    val finishDate: LocalDate
)