package com.example.dynasync.ui.feature.register

sealed interface RegisterUiEvent {
    data object NavigateToHome: RegisterUiEvent
    data object NavigateToLogin: RegisterUiEvent
}