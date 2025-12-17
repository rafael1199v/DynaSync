package com.example.dynasync.ui.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.AuthRepository
import com.example.dynasync.data.repository.ProjectRepository
import com.example.dynasync.data.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(value = HomeViewState())
    val state = _state.asStateFlow()

    init {
        onIntent(HomeIntent.LoadDashboardData)
    }

    private fun getProjects() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            val userId = AuthRepository.getUserId()

            if(userId == null) {
                _state.update {
                    Log.e("debug","Hubo un error al cargar los proyectos")
                    it.copy(error = "Hubo un error al cargar los proyectos", isLoading = false)
                }
            }
            else {
                val projects = ProjectRepository.getProjects(userId)
                 Log.d("debug",projects.toString())

                _state.update { currentState ->
                    currentState.copy(
                        projectsInProcess = 2,
                        pendingTasks = 3,
                        projects = projects,
                        isLoading = false
                    )
                }
            }

        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            try {
                val userId = AuthRepository.getUserId()

                if(userId == null) {
                    _state.update {
                        Log.e("debug","Hubo un error al cargar los proyectos")
                        it.copy(error = "Hubo un error al cargar los proyectos")
                    }
                }
                else {
                    val projectsDeferred = async { ProjectRepository.getProjects(userId) }
                    val userDeferred = async { UserRepository.getUser() }

                    val projects = projectsDeferred.await()
                    val user = userDeferred.await()

                    if(user == null) {
                        _state.update {
                            Log.e("debug", "Hubo un error al cargar los datos")
                            it.copy(error = "Hubo un error al cargar los datos del usuario", isLoading = false)
                        }
                    }
                    else {
                        Log.d("debug",projects.toString())
                        Log.d("debug", user.toString())

                        val projectsInProcess = projects.count { it.tasks.any { task -> !task.isCompleted } }
                        val pendingTasks = projects.sumOf { project ->
                            project.tasks.count { !it.isCompleted }
                        }

                        _state.update { currentState ->
                            currentState.copy(
                                user = user,
                                projectsInProcess = projectsInProcess,
                                pendingTasks = pendingTasks,
                                projects = projects,
                                isLoading = false
                            )
                        }
                    }
                }
            }
            catch (e: Exception){
                _state.update {
                    it.copy(error = "Hubo un error al cargar los datos", isLoading = false)
                }
            }


        }
    }

    private fun onCleanError() {
        _state.update {
            it.copy(error = null)
        }
    }

    fun onIntent(intent: HomeIntent) {
        when(intent) {
            is HomeIntent.LoadProjects -> {
                getProjects()
            }
            is HomeIntent.LoadUser -> {
                viewModelScope.launch {
                    val user = UserRepository.getUser()
                }
            }

            is HomeIntent.LoadDashboardData -> {
                loadDashboardData()
            }

            is HomeIntent.CleanError -> {
                onCleanError()
            }
        }
    }
}