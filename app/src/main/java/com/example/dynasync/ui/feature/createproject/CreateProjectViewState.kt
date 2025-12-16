package com.example.dynasync.ui.feature.createproject

data class CreateProjectViewState(
    val title: String = "",
    val objective: String = "",
    val description: String = "",
    val finishDate: String = "",
    val imageUrl: String = "",

    val titleError: String? = null,
    val objectiveError: String? = null,
    val descriptionError: String? = null,
    val finishDateError: String? = null,
    val imageUrlError: String? = null,

    val isLoading: Boolean = false,
    val isEditMode: Boolean = false
)