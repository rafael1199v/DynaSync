package com.example.dynasync.ui.feature.profile

import com.example.dynasync.domain.model.User

data class ProfileViewState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,

    val userEmail: String? = null
)
