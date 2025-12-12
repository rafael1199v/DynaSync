package com.example.dynasync.ui.feature.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.StaffRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StaffViewModel: ViewModel() {

    private val _state = MutableStateFlow(StaffViewState())
    val state = _state.asStateFlow()

    init {
        onIntent(StaffIntent.LoadStaff)
    }

    fun onIntent(intent: StaffIntent) {
        when(intent) {
            StaffIntent.LoadStaff -> {
                getStaff()
            }
        }
    }

    fun getStaff(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            try {
                val staff = StaffRepository.getStaff()

                _state.update {
                    it.copy(
                        staff = staff
                    )
                }
            }
            catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = "Hubo un error al cargar a los empleados",
                    )
                }
            }
            finally {
                _state.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }

        }
    }

}