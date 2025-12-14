package com.example.dynasync.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.UserRepository
import com.example.dynasync.ui.feature.createproject.CreateProjectUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val _state = MutableStateFlow(value = ProfileViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<ProfileUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        onIntent(ProfileIntent.LoadUser)
    }

    fun onIntent(intent: ProfileIntent) {
        when(intent) {
           is ProfileIntent.LoadUser -> loadUser()
           is ProfileIntent.Logout -> {
                logout()
           }
        }

    }

    private fun loadUser() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val user = UserRepository.getUser("uuid")
                _state.update {
                    it.copy(
                        user = user,
                        error = null
                    )
                }
            }
            catch (e: Exception) {
                _state.update {
                    it.copy(error = "Error al cargar el usuario")
                }
            }
            finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiEvent.send(ProfileUiEvent.NavigateToLogin)
        }
    }
}