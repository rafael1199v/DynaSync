package com.example.dynasync.domain

import kotlinx.datetime.LocalDate

data class Project(
    val id: Int,
    val title: String,
    val objective: String,
    val description: String,
    val finishDate: LocalDate,
    val imageUrl: String,
    val tasks: List<Task>
)
