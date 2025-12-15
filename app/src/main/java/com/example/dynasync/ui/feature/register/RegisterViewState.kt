package com.example.dynasync.ui.feature.register

data class RegisterViewState(
    val name: String = "",
    val lastname: String = "",
    val age: String = "",
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val profileImageUrl: String? = null,


    val nameError: String? = null,
    val lastnameError: String? = null,
    val ageError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val repeatPasswordError: String? = null,
    val profileImageUrlError: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditMode: Boolean = false
)
