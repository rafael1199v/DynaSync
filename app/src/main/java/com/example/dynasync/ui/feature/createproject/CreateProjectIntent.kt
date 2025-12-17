package com.example.dynasync.ui.feature.createproject

sealed interface CreateProjectIntent {
    data class TitleChange(val title: String) : CreateProjectIntent
    data class ObjectiveChange(val objective: String) : CreateProjectIntent
    data class DescriptionChange(val description: String) : CreateProjectIntent
    data class FinishDateChange(val finishDate: String) : CreateProjectIntent
    data class ImageUrlChange(val imageUrl: String) : CreateProjectIntent
    data object SubmitProjectForm : CreateProjectIntent

    data class LoadProject(val projectId: Int): CreateProjectIntent

    data object CleanError: CreateProjectIntent
}