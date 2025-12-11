package com.example.dynasync.ui.feature.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val _state = MutableStateFlow(value = LoginViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun updateEmail(newEmailValue: String) {
        _state.update { it.copy(email = newEmailValue) }
    }

    fun updatePassword(newPasswordValue: String) {
        _state.update { it.copy(password = newPasswordValue) }
    }

    fun validateEmail() {
        val emailInput = _state.value.email.trim()

        if (emailInput.isEmpty()) {
            _state.update { it.copy(emailError = "El correo es obligatorio") }
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            _state.update { it.copy(emailError = "Formato de correo inválido") }
        } else {
            _state.update { it.copy(emailError = null) }
        }
    }

    fun validatePassword() {
        val passwordInput = _state.value.password.trim()

        if (passwordInput.isEmpty()) {
            _state.update { it.copy(passwordError = "La contraseña es obligatoria") }
            return
        }

        if (passwordInput.length < 8) {
            _state.update { it.copy(passwordError = "La contraseña debe tener al menos 8 caracteres") }
        } else {
            _state.update { it.copy(passwordError = null) }
        }
    }

    fun submitForm() {

        validateEmail()
        validatePassword()

        if(_state.value.emailError == null && _state.value.passwordError == null) {
            viewModelScope.launch {
                // Aquí iría tu llamada a la API (Retrofit/Firebase)
                // if (apiResponse.isSuccess) {

                // 2. Emitimos el evento de navegación
                _uiEvent.send(LoginUiEvent.NavigateToHome)

                // }
            }
        }
    }
}