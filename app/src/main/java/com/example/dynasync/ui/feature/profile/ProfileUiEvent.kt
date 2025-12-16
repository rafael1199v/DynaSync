package com.example.dynasync.ui.feature.profile

sealed interface ProfileUiEvent {
    data object NavigateToLogin: ProfileUiEvent
}