package com.example.dynasync.ui.feature.staff

import com.example.dynasync.domain.model.Personal

data class StaffViewState(
    val isLoading: Boolean = false,
    val staff: List<Personal> = emptyList(),
    val error: String? = null
)
