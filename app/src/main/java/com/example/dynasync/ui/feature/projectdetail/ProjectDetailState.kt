package com.example.dynasync.ui.feature.projectdetail

import com.example.dynasync.domain.Project
data class ProjectDetailState(
    val project: Project? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
