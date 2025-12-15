package com.example.dynasync.data.repository

import android.util.Log
import androidx.compose.animation.core.copy
import com.example.dynasync.data.mapper.toDto
import com.example.dynasync.data.mapper.toUpdateDto
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
        try {
            supabase.from("tasks").delete {
                filter {
                    eq("id", taskId)
                }
            }
        }
        catch (e: Exception) {
            Log.e("debug", "Error eliminando la tarea de la BD: $e")
            throw e
        }

    }

    suspend fun updateTask(task: Task) {
        try {
            Log.d("debug", "updateTask: $task")
            supabase.from("tasks").update(task.toUpdateDto()) {
                filter {
                    eq("id", task.id)
                }
            }
        }
        catch (e: Exception) {
            Log.e("debug", "Error actualizando la tarea de la BD: $e")
            throw e
        }

    }

    suspend fun toggleTask(taskId: Int, actualStatus: Boolean) {
        val newStatus = !actualStatus

        try {
            supabase.from("tasks").update(mapOf("is_completed" to newStatus)) {
                filter {
                    eq("id", taskId)
                }
            }
        }
        catch (e: Exception) {
            Log.e("debug", "Error actualizando la tarea de la BD: $e")
            throw e
        }
    }

}