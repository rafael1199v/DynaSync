package com.example.dynasync.ui.feature.register

import android.util.Log
import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.dto.ProfileCreateDto
import com.example.dynasync.data.repository.RegisterRepository
import com.example.dynasync.ui.feature.register.validators.RegisterFormValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class RegisterViewModel: ViewModel() {
    private val _state = MutableStateFlow(value = RegisterViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<RegisterUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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

            is RegisterIntent.SubmitForm -> {
                onSubmitForm()
            }

            is RegisterIntent.NavigateToLogin -> {
                viewModelScope.launch {
                    _uiEvent.send(RegisterUiEvent.NavigateToLogin)
                }
            }
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
                    Log.d("debug", "onSubmitForm: ${state.value}")

                    val imagePath = validatedState.profileImageUrl
                    var imageBytes: ByteArray? = null

                    if(imagePath.isNotEmpty()){
                        imageBytes = File(imagePath).readBytes()
                    }

                    RegisterRepository.createUser(
                        profileCreateDto = ProfileCreateDto(
                            email = state.value.email,
                            name = state.value.name,
                            lastname = state.value.lastname,
                            age = state.value.age.toInt(),
                            imageUrl = state.value.profileImageUrl.ifEmpty { null },
                            password = state.value.password
                        ),
                        newImageBytes = imageBytes
                    )

                    _uiEvent.send(RegisterUiEvent.NavigateToHome)

                } catch (e: Exception) {
                    val errorMsg = e.message ?: ""

                    Log.e("debug", "Ocurrió un error: $errorMsg")

                    if (errorMsg.contains("user_already_exists") || errorMsg.contains("User already registered")) {
                        _state.update { it.copy(error = "Este correo ya está registrado.") }
                    } else {
                        _state.update { it.copy(error = "Error al registrarse. Revisa tu conexión a internet.") }
                    }
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