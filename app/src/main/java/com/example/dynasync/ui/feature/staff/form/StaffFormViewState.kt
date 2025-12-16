package com.example.dynasync.ui.feature.staff.form

data class StaffFormViewState(
    val name: String = "",
    val lastname: String = "",
    val charge: String = "",
    val imageUrl: String? = null,

    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,

    val nameError: String? = null,
    val lastnameError: String? = null,
    val chargeError: String? = null,

    val error: String? = null
)
