package com.example.dynasync.ui.feature.staff

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.StaffRepository
import com.example.dynasync.ui.feature.staff.form.StaffFormUiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StaffViewModel: ViewModel() {

    private val _state = MutableStateFlow(StaffViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<StaffUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        onIntent(StaffIntent.LoadStaff)
    }

    fun onIntent(intent: StaffIntent) {
        when(intent) {
            is StaffIntent.LoadStaff -> {
                getStaff()
            }

            is StaffIntent.DeleteStaff -> {

            }
            is StaffIntent.UpdateStaff -> {
                onUpdateStaff(intent.staffId)
            }
        }

    }

    private fun onUpdateStaff(staffId: Int) {
        viewModelScope.launch {
            _uiEvent.send(StaffUiEvent.NavigateToEditForm(staffId = staffId))
        }
    }

    private fun getStaff(){
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