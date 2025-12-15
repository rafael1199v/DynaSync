package com.example.dynasync.ui.feature.register

sealed interface RegisterIntent {

    data class ChangeName(val name: String) : RegisterIntent
    data class ChangeLastName(val lastname: String): RegisterIntent
    data class ChangeAge(val age: String): RegisterIntent
    data class ChangeEmail(val email: String): RegisterIntent
    data class ChangePassword(val password: String): RegisterIntent
    data class ChangeRepeatPassword(val repeatPassword: String): RegisterIntent
    data class ChangeProfileImageUrl(val profileImageUrl: String): RegisterIntent

    data object NavigateToLogin : RegisterIntent

    object SubmitForm: RegisterIntent
}