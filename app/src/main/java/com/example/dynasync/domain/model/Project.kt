package com.example.dynasync.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.Instant

data class Project(
    val id: Int,
    val title: String,
    val objective: String,
    val description: String,
    val finishDate: LocalDate,
    val imageUrl: String?,
    val tasks: List<Task>,
    val createdAt: Instant? = null
)


fun Project.calculateCompletePercentage() : Float {
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }

    return if(totalTasks == 0)
        0f
    else
        (completedTasks.toFloat() / totalTasks.toFloat())
}
