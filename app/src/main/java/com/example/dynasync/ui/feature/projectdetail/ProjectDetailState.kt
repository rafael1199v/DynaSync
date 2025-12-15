package com.example.dynasync.ui.feature.projectdetail

import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Project
data class ProjectDetailState(
    val project: Project? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val staffList: List<Personal> = emptyList(),

    val isInitLoading: Boolean = false
)
