package com.example.dynasync.ui.feature.login

sealed interface LoginUiEvent {
    object NavigateToHome : LoginUiEvent
    object NavigateToRegister: LoginUiEvent
}