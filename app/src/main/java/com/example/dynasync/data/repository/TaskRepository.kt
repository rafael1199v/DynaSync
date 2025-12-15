package com.example.dynasync.data.repository

import androidx.compose.animation.core.copy
import com.example.dynasync.data.mapper.toDto
import com.example.dynasync.data.supabase.SupabaseClientObject
import com.example.dynasync.domain.model.Task
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.delay
import kotlin.time.Clock

object TaskRepository {
    val projects = ProjectRepository.projects
    val supabase = SupabaseClientObject.client

    suspend fun addTask(newTask: Task, projectId: Int) {
        val createdTask = newTask.copy(
            createdAt = Clock.System.now()
        )

        val taskDto = createdTask.toDto(projectId)

        supabase.from("tasks").insert(taskDto)

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

    suspend fun toggleTask(taskId: Int) {
        delay(2000)

        for (project in projects) {
            val index = project.tasks.indexOfFirst { it.id == taskId }

            if(index != -1) {
                val toggleTask = project.tasks[index].copy(
                    isCompleted = !project.tasks[index].isCompleted,
                )

                val updatedTasks = project.tasks.toMutableList()
                updatedTasks[index] = toggleTask

                val updatedProject = project.copy(tasks = updatedTasks)
                val indexProject = projects.indexOfFirst { it.id == project.id }
                projects[indexProject] = updatedProject
            }
        }
    }

}