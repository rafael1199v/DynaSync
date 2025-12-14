package com.example.dynasync.data.repository

import androidx.compose.animation.core.copy
import com.example.dynasync.domain.model.Task
import kotlinx.coroutines.delay
import kotlin.time.Clock

object TaskRepository {
    val projects = ProjectRepository.projects

    suspend fun addTask(newTask: Task, projectId: Int) {
        delay(2000)
        val projectIndex = projects.indexOfFirst { it.id == projectId }
        if (projectIndex != -1) {
            val project = projects[projectIndex]

            val maxId = project.tasks.maxByOrNull { it.id }?.id ?: 0
            val newId = maxId + 1

            val creationTime = newTask.createdAt ?: Clock.System.now()

            val finalTask = newTask.copy(
                id = newId,
                createdAt = creationTime
            )

            val updatedTasks = project.tasks + finalTask
            val updatedProject = project.copy(tasks = updatedTasks)

            projects[projectIndex] = updatedProject
        }

        println("La nueva tarea agregada es ${newTask}")
    }
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

    suspend fun updateTask(task: Task) {
        delay(2000)

        for (project in projects) {
            val index = project.tasks.indexOfFirst { it.id == task.id }

            if(index != -1) {
                val newTask = project.tasks[index].copy(
                    title = task.title,
                    isCompleted = task.isCompleted,
                    personal = task.personal,
                    finishDate = task.finishDate
                )

                val updatedTasks = project.tasks.toMutableList()
                updatedTasks[index] = newTask

                val updatedProject = project.copy(tasks = updatedTasks)
                val indexProject = projects.indexOfFirst { it.id == project.id }
                projects[indexProject] = updatedProject

            }
        }
    }

}