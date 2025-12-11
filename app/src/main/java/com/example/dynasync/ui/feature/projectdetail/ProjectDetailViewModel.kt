package com.example.dynasync.ui.feature.projectdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dynasync.data.repository.ProjectRepository
import com.example.dynasync.data.repository.ProjectRepository.getProjectById
import com.example.dynasync.navigation.MainDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectDetailViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<MainDestination.ProjectDetail>()

    private val _state = MutableStateFlow(value = ProjectDetailState())
    val state = _state.asStateFlow()

    val projectId = args.projectId

    init {
        _state.update {
            it.copy(isLoading = true)
        }

        onIntent(ProjectDetailIntent.LoadProject(projectId))
    }

    fun onIntent(intent: ProjectDetailIntent) {
        when(intent) {
            is ProjectDetailIntent.LoadProject -> {
                getProjectById(intent.projectId)
            }
            is ProjectDetailIntent.DeleteProject -> {
                println("Eliminando el proyecto ${intent.projectId}")
            }
            is ProjectDetailIntent.EditProject -> {
                println("Editando el proyecto ${intent.projectId}")
            }
            is ProjectDetailIntent.DeleteTask -> {
                println("Eliminando la tarea ${intent.taskId}")
            }
            is ProjectDetailIntent.ToggleTask -> {
                println("Toggle la tarea ${intent.taskId}")
            }
        }
    }

    private fun getProjectById(projectId: Int) {
        viewModelScope.launch {
            val project = ProjectRepository.getProjectById(projectId = projectId)

            _state.update {
                it.copy(
                    project = project,
                    isLoading = false
                )
            }
        }
    }
}