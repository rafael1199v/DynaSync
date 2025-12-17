package com.example.dynasync.ui.feature.createproject.validators

import com.example.dynasync.ui.feature.createproject.CreateProjectViewState

class ProjectFormValidator {

    fun validate(state: CreateProjectViewState): CreateProjectViewState {
        return state.copy(
            titleError = validateTitle(state.title),
            objectiveError = validateObjective(state.objective),
            descriptionError = validateDescription(state.description),
            finishDateError = validateFinishDate(state.finishDate),
            imageUrlError = validateImage(state.imageUrl)
        )
    }

    fun isValid(state: CreateProjectViewState): Boolean {
        return state.titleError == null &&
                state.objectiveError == null &&
                state.descriptionError == null &&
                state.finishDateError == null &&
                state.title.isNotBlank() &&
                state.description.isNotBlank()
    }

    private fun validateTitle(title: String): String? {
        return when {
            title.isBlank() -> "El título es obligatorio."
            title.length < 5 -> "El título debe tener al menos 5 caracteres."
            title.length > 50 -> "El título no puede exceder los 50 caracteres."
            else -> null
        }
    }

    private fun validateObjective(objective: String): String? {
        return when {
            objective.isBlank() -> "El objetivo es obligatorio."
            objective.length > 100 -> "El objetivo debe ser breve (máx 100 caracteres)."
            else -> null
        }
    }

    private fun validateDescription(description: String): String? {
        return when {
            description.isBlank() -> "La descripción es obligatoria."
            else -> null
        }
    }

    private fun validateFinishDate(date: String): String? {
        return if (date.isBlank()) "Debes seleccionar una fecha de finalización." else null
    }

    private fun validateImage(url: String): String? {
        return null
    }
}