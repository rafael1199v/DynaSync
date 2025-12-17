package com.example.dynasync.ui.feature.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.UserRepository
import com.example.dynasync.ui.feature.createproject.CreateProjectUiEvent
import com.example.dynasync.ui.theme.AppThemeMode
import com.example.dynasync.ui.theme.ThemeManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    val currentTheme = ThemeManager.themeMode
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
           is ProfileIntent.Logout -> logout()
            is ProfileIntent.ChangeTheme -> {
                Log.d("ProfileViewModel", "Cambiando tema: ${intent.newTheme}")
                onChangeAppTheme(intent.newTheme)
            }
        }

    }

    private fun loadUser() {
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val user = UserRepository.getUser()
                val userEmail = UserRepository.getUserEmail()

                _state.update {
                    it.copy(
                        user = user,
                        userEmail = userEmail,
                        error = null
                    )
                }
                if(user == null) {
                    _state.update {
                        it.copy(error = "Error al cargar el usuario. Revisa tu conexión a internet.")
                    }
                }
            }
            catch (e: Exception) {
                _state.update {
                    it.copy(error = "Error al cargar el usuario. Revisa tu conexión a internet.")
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

    private fun onChangeAppTheme(newAppTheme: AppThemeMode) {
        ThemeManager.saveTheme(newAppTheme)
    }
}