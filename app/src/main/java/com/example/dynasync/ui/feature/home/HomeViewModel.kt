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
        _state.update {
            it.copy(isLoading = true)
        }

        onIntent(HomeIntent.LoadDashboardData)

        _state.update {
            it.copy(isLoading = false)
        }
    }

    private fun getProjects() {
        viewModelScope.launch {

            val userId = AuthRepository.getUserId()

            if(userId == null) {
                _state.update {
                    Log.e("debug","Hubo un error al cargar los proyectos")
                    it.copy(error = "Hubo un error al cargar los proyectos")
                }
            }
            else {
                val projects = ProjectRepository.getProjects(userId)
                 Log.d("debug",projects.toString())

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

    private fun loadDashboardData() {
        viewModelScope.launch {

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
                        it.copy(error = "Hubo un error al cargar los datos del usuario")
                    }
                }
                else {
                    Log.d("debug",projects.toString())
                    Log.d("debug", user.toString())

                    _state.update { currentState ->
                        currentState.copy(
                            user = user,
                            projectsInProcess = 2,
                            pendingTasks = 3,
                            projects = projects
                        )
                    }
                }
            }

        }
    }

    fun onIntent(intent: HomeIntent) {
        when(intent) {
            is HomeIntent.LoadProjects -> {
                getProjects()
            }
            HomeIntent.LoadUser -> {
                viewModelScope.launch {
                    val user = UserRepository.getUser()
                }
            }

            HomeIntent.LoadDashboardData -> {
                loadDashboardData()
            }
        }
    }
}