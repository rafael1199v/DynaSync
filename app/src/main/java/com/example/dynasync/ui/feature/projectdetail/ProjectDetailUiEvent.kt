package com.example.dynasync.ui.feature.projectdetail

sealed interface ProjectDetailUiEvent {
    data object NavigateToHome : ProjectDetailUiEvent
    data class NavigateToEditProject(val projectId: Int): ProjectDetailUiEvent
}