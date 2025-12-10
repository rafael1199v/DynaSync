package com.example.dynasync.viewmodels
import androidx.lifecycle.ViewModel
import com.example.dynasync.data.ProjectRepository
import com.example.dynasync.ui.states.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(value = HomeViewState())
    val state = _state.asStateFlow()

    init {
        _state.value = HomeViewState(
            projectsInProcess = 2,
            pendingTasks = 3,
            projects = ProjectRepository.projects
        )

    }
}