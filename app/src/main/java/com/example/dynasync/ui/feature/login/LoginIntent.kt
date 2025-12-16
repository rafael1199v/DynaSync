package com.example.dynasync.ui.feature.login

sealed interface LoginIntent {
    data class EmailChanged(val email: String): LoginIntent
    data class PasswordChanged(val password: String): LoginIntent
    data object SubmitLogin : LoginIntent

    data object NavigateToRegister: LoginIntent
}