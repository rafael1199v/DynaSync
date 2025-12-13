package com.example.dynasync.data.repository

import kotlinx.coroutines.delay

object TaskRepository {
    val projects = ProjectRepository.projects

    suspend fun deleteTask(taskId: Int) {
        delay(2000)
        for (project in projects) {
            val task = project.tasks.find { it.id == taskId }
            if (task != null) {
                val newProject = project.copy(tasks = project.tasks - task)
                val index = projects.indexOf(project)
                projects[index] = newProject
                break
            }
        }
    }

}