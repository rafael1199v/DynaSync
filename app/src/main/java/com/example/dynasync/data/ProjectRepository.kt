package com.example.dynasync.data

import com.example.dynasync.domain.Project
import com.example.dynasync.domain.Task
import kotlinx.datetime.LocalDate

object ProjectRepository {
    val projects: List<Project> = listOf(
        Project(
            id = 1,
            title = "DynaSync",
            objective = "Mejorar la productividad de las personas",
            description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839257864-c6ccab7238de?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = listOf(

            )
        ),
        Project(
            id = 2,
            title = "DynaSync 2",
            objective = "Mejorar la productividad de las personas",
            description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839257864-c6ccab7238de?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = emptyList()
        )
    )

    suspend fun getProjectById(projectId: Int): Project? {
        return projects.find { it.id == projectId }
    }
}