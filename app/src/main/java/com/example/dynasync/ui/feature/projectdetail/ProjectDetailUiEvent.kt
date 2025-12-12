package com.example.dynasync.ui.feature.projectdetail

sealed interface ProjectDetailUiEvent {
    data object NavigateToHome : ProjectDetailUiEvent
}