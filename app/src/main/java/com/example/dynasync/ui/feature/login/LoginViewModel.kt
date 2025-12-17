package com.example.dynasync.ui.feature.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.AuthRepository
import com.example.dynasync.data.repository.AuthResult
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


    fun onIntent(intent: LoginIntent) {
        when(intent) {
            is LoginIntent.EmailChanged -> updateEmail(intent.email)
            is LoginIntent.PasswordChanged -> updatePassword(intent.password)
            is LoginIntent.SubmitLogin -> submitForm()
            is LoginIntent.NavigateToRegister -> {
                onRegisterButtonClick()
            }
        }
    }

    private fun updateEmail(newEmailValue: String) {
        _state.update { it.copy(email = newEmailValue, emailError = null) }
    }

    private fun updatePassword(newPasswordValue: String) {
        _state.update { it.copy(password = newPasswordValue, passwordError = null) }
    }

    private fun validateEmail() : Boolean {
        val emailInput = _state.value.email.trim()

        if (emailInput.isEmpty()) {
            _state.update { it.copy(emailError = "El correo es obligatorio") }
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            _state.update { it.copy(emailError = "Formato de correo inválido") }
            return false
        } else {
            _state.update { it.copy(emailError = null) }
            return true
        }
    }

    private fun validatePassword(): Boolean {
        val passwordInput = _state.value.password.trim()

        if (passwordInput.isEmpty()) {
            _state.update { it.copy(passwordError = "La contraseña es obligatoria") }
            return false
        }

        if (passwordInput.length < 8) {
            _state.update { it.copy(passwordError = "La contraseña debe tener al menos 8 caracteres") }
            return false
        } else {
            _state.update { it.copy(passwordError = null) }
            return true
        }
    }

    private fun submitForm() {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()

        if (isEmailValid && isPasswordValid) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                val result = AuthRepository.signIn(_state.value.email, _state.value.password)


                if(result is AuthResult.Error) {
                    _state.update { it.copy(loginError = result.message) }
                }
                else {
                    _uiEvent.send(LoginUiEvent.NavigateToHome)
                }


                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    private fun onRegisterButtonClick() {
        viewModelScope.launch {
            _uiEvent.send(LoginUiEvent.NavigateToRegister)
        }
    }
}