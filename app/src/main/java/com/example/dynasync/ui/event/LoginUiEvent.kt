package com.example.dynasync.ui.event

sealed interface LoginUiEvent {
    object NavigateToHome : LoginUiEvent
}