package com.example.dynasync.ui.feature.home

import com.example.dynasync.domain.model.Project
import com.example.dynasync.domain.model.User

data class HomeViewState(
    val projectsInProcess: Int = 0,
    val pendingTasks: Int = 0,
    val projects: List<Project> = emptyList(),
    val user: User = User(
        id = "uuid",
        name = "",
        lastName = "",
        age = 20,
        profileImageUrl = ""
    ),

    val isLoading: Boolean = false,
    val error: String? = null
)