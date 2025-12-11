package com.example.dynasync.ui.states

data class LoginViewState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,

    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null
)
