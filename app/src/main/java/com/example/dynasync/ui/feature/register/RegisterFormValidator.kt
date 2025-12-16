package com.example.dynasync.ui.feature.register

import android.util.Patterns

class RegisterFormValidator {
    fun validate(state: RegisterViewState): RegisterViewState {
        return state.copy(
            nameError = validateName(state.name),
            lastnameError = validateLastname(state.lastname),
            ageError = validateAge(state.age),
            emailError = validateEmail(state.email),
            passwordError = validatePassword(state.password),
            repeatPasswordError = validateRepeatPassword(state.password, state.repeatPassword)
        )
    }

    fun isValid(state: RegisterViewState): Boolean {
        val validatedState = validate(state)
        return validatedState.nameError == null &&
                validatedState.lastnameError == null &&
                validatedState.ageError == null &&
                validatedState.emailError == null &&
                validatedState.passwordError == null &&
                validatedState.repeatPasswordError == null
    }

    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio."
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres."
            else -> null
        }
    }

    private fun validateLastname(lastname: String): String? {
        return when {
            lastname.isBlank() -> "El apellido es obligatorio."
            lastname.length < 3 -> "El apellido debe tener al menos 3 caracteres."
            else -> null
        }
    }

    private fun validateAge(age: String): String? {
        return when {
            age.isBlank() -> "La edad es obligatoria."
            age.toIntOrNull() == null -> "La edad debe ser un número."
            age.toInt() < 18 -> "Debes ser mayor de 18 años."
            else -> null
        }
    }

    private fun validateEmail(email: String): String? {
        return when {
            email.isBlank() -> "El correo es obligatorio."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "El formato del correo no es válido."
            else -> null
        }
    }

    private fun validatePassword(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña es obligatoria."
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
            else -> null
        }
    }

    private fun validateRepeatPassword(password: String, repeatPassword: String): String? {
        return when {
            repeatPassword.isBlank() -> "Debes repetir la contraseña."
            password != repeatPassword -> "Las contraseñas no coinciden."
            else -> null
        }
    }
}