package com.example.dynasync.ui.feature.projectdetail

sealed interface ProjectDetailIntent {
    data class LoadProject(val projectId: Int) : ProjectDetailIntent
    data class DeleteProject(val projectId: Int) : ProjectDetailIntent
    data class EditProject(val projectId: Int) : ProjectDetailIntent

    data class ToggleTask(val taskId: Int) : ProjectDetailIntent
    data class DeleteTask(val taskId: Int) : ProjectDetailIntent
}
