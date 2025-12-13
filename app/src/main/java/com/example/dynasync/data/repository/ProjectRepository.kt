package com.example.dynasync.data.repository

import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Project
import com.example.dynasync.domain.model.Task
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Instant

object ProjectRepository {
    val projects: MutableList<Project> = mutableListOf(
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
                    createdAt = Instant.parse("2023-11-01T12:00:00Z")
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
                    createdAt = Instant.parse("2023-11-01T12:00:00Z")
                ),

            ),
            createdAt = Instant.parse("2023-11-01T12:00:00Z")
        ),
        Project(
            id = 2,
            title = "DynaSync 2",
            objective = "Mejorar la productividad de las personas",
            description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839256791-6a93f89fb8b0?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = emptyList(),
            createdAt = Instant.parse("2022-11-01T12:00:00Z")
        )
    )

    //val projects = emptyList<Project>()

    suspend fun getProjectById(projectId: Int): Project? {
        return projects.find { it.id == projectId }
    }

    suspend fun getProjects() : List<Project> {
        return projects
    }

    suspend fun addProject(newProject: Project) {
        delay(2000)
        val newId = (projects.maxByOrNull { it.id }?.id ?: 0) + 1
        val creationTime = newProject.createdAt ?: Clock.System.now()
        projects.add(newProject.copy(
            id = newId,
            createdAt = creationTime
        ))
    }

    suspend fun deleteProject(projectId: Int) {
        delay(2000)
        projects.removeIf { it.id == projectId }
    }

    suspend fun updateProject(projectUpdated: Project) {
        delay(2000)
        val index = projects.indexOfFirst { it.id == projectUpdated.id }

        if (index != -1) {
            projects[index] = projectUpdated
        }
    }
}