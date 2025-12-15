package com.example.dynasync.ui.feature.register

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _state = MutableStateFlow(value = RegisterViewState())
    val state = _state.asStateFlow()

    val validator = RegisterFormValidator()

    fun onIntent(intent: RegisterIntent) {
        when(intent) {
            is RegisterIntent.ChangeEmail -> {
                onChangeEmail(intent.email)
            }
            is RegisterIntent.ChangeName -> {
                onChangeName(intent.name)
            }
            is RegisterIntent.ChangeLastName -> {
                onChangeLastName(intent.lastname)
            }
            is RegisterIntent.ChangePassword -> {
                onChangePassword(intent.password)
            }
            is RegisterIntent.ChangeRepeatPassword -> {
                onChangeRepeatPassword(intent.repeatPassword)
            }
            is RegisterIntent.ChangeAge -> {
                onChangeAge(intent.age)
            }
            is RegisterIntent.ChangeProfileImageUrl -> {
                onChangeProfileImageUrl(intent.profileImageUrl)
            }

            is RegisterIntent.SubmitForm -> {}
        }
    }


    private fun onChangeEmail(email: String) {
        _state.update {
            it.copy(email = email, emailError = null)
        }
    }

    private fun onChangeName(name: String) {
        _state.update {
            it.copy(name = name, nameError = null)
        }
    }

    private fun onChangeLastName(lastname: String) {
        _state.update {
            it.copy(lastname = lastname, lastnameError = null)
        }
    }

    private fun onChangePassword(password: String) {
        _state.update {
            it.copy(password = password, passwordError = null)
        }
    }

    private fun onChangeRepeatPassword(repeatPassword: String) {
        _state.update {
            it.copy(repeatPassword = repeatPassword, repeatPasswordError = null)
        }
    }

    private fun onChangeAge(age: String) {
        if (age.isEmpty() || age.all { it.isDigit() }) {
            _state.update {
                it.copy(age = age, ageError = null)
            }
        }
    }

    private fun onChangeProfileImageUrl(profileImageUrl: String) {
        _state.update {
            it.copy(profileImageUrl = profileImageUrl)
        }
    }

    private fun onSubmitForm() {
        val validatedState = validator.validate(state.value)

        if (validator.isValid(validatedState)) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
//                    // Aquí iría la lógica para registrar al usuario
//                    // Ejemplo con un repositorio de autenticación
//                    AuthRepository.register(
//                        email = state.value.email,
//                        password = state.value.password,
//                        name = state.value.name,
//                        lastname = state.value.lastname,
//                        age = state.value.age.toInt()
//                    )
//                    _uiEvent.send(RegisterUiEvent.NavigateToHome)

                } catch (e: Exception) {
                    _state.update { it.copy(error = e.message) }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
        else {
            _state.update { validatedState }
        }
    }

}