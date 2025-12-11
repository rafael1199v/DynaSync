package com.example.dynasync.ui.feature.home

import com.example.dynasync.domain.Project
import com.example.dynasync.domain.User

data class HomeViewState(
    val projectsInProcess: Int = 0,
    val pendingTasks: Int = 0,
    val projects: List<Project> = emptyList(),
    val user: User = User(
        id = 1,
        name = "Rafael",
        lastName = "Vargas",
        age = 20,
        profileImageUrl = "https://media.gettyimages.com/id/495992888/es/foto/portland-or-october-16-linus-torvalds-a-software-engineer-and-principal-creator-of-the-linux.jpg?s=1024x1024&w=gi&k=20&c=qNKDim5DkJnPZhA-IUfIOxW60qwujf90Td7sL3FR7GU="
    )
)