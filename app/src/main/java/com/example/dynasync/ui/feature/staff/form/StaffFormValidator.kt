package com.example.dynasync.ui.feature.staff.form

import com.example.dynasync.ui.feature.createproject.CreateProjectViewState
import com.example.dynasync.ui.feature.staff.StaffViewState

class StaffFormValidator {

    fun validate(state: StaffFormViewState): StaffFormViewState {
        return state.copy(
            nameError = validateName(state.name),
            lastnameError = validateLastName(state.lastname),
            chargeError = validateCharge(state.charge),
        )
    }

    fun isValid(state: StaffFormViewState): Boolean {
        return state.nameError == null &&
                state.lastnameError == null &&
                state.chargeError == null &&
                state.name.isNotBlank() &&
                state.lastname.isNotBlank() &&
                state.charge.isNotBlank()
    }

    private fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "El nombre es obligatorio."
            name.length > 20 -> "El nombre no puede exceder los 20 caracteres."
            else -> null
        }
    }

    private fun validateLastName(lastname: String): String? {
        return when {
            lastname.isBlank() -> "El apellido es obligatorio."
            lastname.length > 20 -> "El apellido no puede exceder los 20 caracteres."
            else -> null
        }
    }

    private fun validateCharge(charge: String): String? {
        return when {
            charge.isBlank() -> "El cargo es obligatorio."
            charge.length > 20 -> "El cargo no puede exceder los 20 caracteres"
            else -> null
        }
    }
}