package com.example.dynasync.ui.feature.projectdetail

import com.example.dynasync.domain.model.Personal

sealed interface ProjectDetailIntent {
    data class LoadProject(val projectId: Int) : ProjectDetailIntent
    data class DeleteProject(val projectId: Int) : ProjectDetailIntent
    data class EditProject(val projectId: Int) : ProjectDetailIntent

    data class ToggleTask(val taskId: Int, val actualStatus: Boolean) : ProjectDetailIntent
    data class DeleteTask(val taskId: Int) : ProjectDetailIntent


    data class CreateTask(
        val title: String,
        val staffId: Int?,
        val finishDate: String
    ) : ProjectDetailIntent


    data class UpdateTask(
        val taskId: Int,
        val title: String,
        val staffId: Int?,
        val finishDate: String
    ) : ProjectDetailIntent

}
