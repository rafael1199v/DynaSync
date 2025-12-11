package com.example.dynasync.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(value = HomeViewState())
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            val projects = ProjectRepository.getProjects()

            _state.update { currentState ->
                currentState.copy(
                    projectsInProcess = 2,
                    pendingTasks = 3,
                    projects = projects
                )
            }
        }
    }
}