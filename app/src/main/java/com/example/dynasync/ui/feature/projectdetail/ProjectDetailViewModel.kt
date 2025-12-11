package com.example.dynasync.ui.feature.projectdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dynasync.data.ProjectRepository
import com.example.dynasync.navigation.MainDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<MainDestination.ProjectDetail>()

    private val _state = MutableStateFlow(value = ProjectDetailState())
    val state = _state.asStateFlow()

    init {
        _state.value = _state.value.copy(isLoading = true)
        println(args.projectId)
        getProjectById(args.projectId)
    }

    fun getProjectById(projectId: Int) {
        viewModelScope.launch {
            val project = ProjectRepository.getProjectById(projectId = projectId)
            _state.value = _state.value.copy(project = project)
            _state.value = _state.value.copy(isLoading = false)
        }
    }
}