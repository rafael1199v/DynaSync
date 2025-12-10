package com.example.dynasync.data

import com.example.dynasync.domain.Personal
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
                Task(
                    id = 1,
                    title = "Task 1",
                    description = "Description of task 1",
                    isCompleted = true,
                    personal = Personal(
                        id = 1,
                        name = "Carlos",
                        lastname = "Salazar",
                        charge = "Developer"
                    ),
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                ),
                Task(
                    id = 2,
                    title = "Task 2",
                    description = "Description of task 2",
                    isCompleted = false,
                    personal = Personal(
                        id = 1,
                        name = "Rafael",
                        lastname = "Vargas",
                        charge = "Developer"
                    ),
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                )
            )
        ),
        Project(
            id = 2,
            title = "DynaSync 2",
            objective = "Mejorar la productividad de las personas",
            description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839256791-6a93f89fb8b0?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = emptyList()
        )
    )

    //val projects = emptyList<Project>()

    suspend fun getProjectById(projectId: Int): Project? {
        return projects.find { it.id == projectId }
    }
}