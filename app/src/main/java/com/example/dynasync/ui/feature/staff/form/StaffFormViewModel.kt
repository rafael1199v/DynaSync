package com.example.dynasync.ui.feature.staff.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dynasync.data.repository.AuthRepository
import com.example.dynasync.data.repository.StaffRepository
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.navigation.MainDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class StaffFormViewModel(
    stateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(value = StaffFormViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<StaffFormUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val staffFormValidator = StaffFormValidator()
    val args = stateHandle.toRoute<MainDestination.StaffForm>()
    val staffId = args.staffId

    init {
        if(staffId != -1) {
            _state.update {
                it.copy(isEditMode = true)
            }

            onIntent(StaffFormIntent.LoadStaff(staffId))
        }
    }

    fun onIntent(intent: StaffFormIntent) {
        when(intent) {
            is StaffFormIntent.NameChange -> {
                onChangeName(intent.name)
            }
            is StaffFormIntent.LastNameChange -> {
                onChangeLastName(intent.lastname)
            }
            is StaffFormIntent.ChargeChange -> {
                onChangeCharge(intent.charge)
            }
            is StaffFormIntent.ImageUrlChange -> {
                onChangeImageUrl(intent.imageUrl)
            }
            is StaffFormIntent.LoadStaff -> {
                loadStaff(intent.staffId)
            }

            is StaffFormIntent.SubmitForm -> {
                onSubmitForm()
            }
        }
    }

    private fun loadStaff(staffId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val staff = StaffRepository.getStaffById(staffId)

            _state.update {
                it.copy(
                    name = staff?.name ?: "",
                    lastname = staff?.lastname ?: "",
                    charge = staff?.charge ?: "",
                    imageUrl = staff?.imageUrl,
                    isLoading = false
                )
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun onChangeName(name: String) {
        _state.update {
            it.copy(name = name)
        }
    }

    private fun onChangeLastName(lastname: String) {
        _state.update {
            it.copy(lastname = lastname)
        }
    }

    private fun onChangeCharge(charge: String) {
        _state.update {
            it.copy(charge = charge)
        }
    }

    private fun onChangeImageUrl(imageUrl: String?) {
        _state.update {
            it.copy(imageUrl = imageUrl)
        }
    }

    private fun onSubmitForm() {
        val newState = staffFormValidator.validate(state.value)

        if(staffFormValidator.isValid(newState)) {


            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                _state.update { it.copy(error = null) }


                try {

                    val imagePath = newState.imageUrl
                    var imageBytes: ByteArray? = null

                    val isRemoteUrl = imagePath?.startsWith("http")
                    val isLocalFile = imagePath?.isNotEmpty() == true && !isRemoteUrl!!

                    if (isLocalFile) {
                        imageBytes = File(imagePath).readBytes()
                    }

                    val urlToSend = if (isLocalFile) {
                        if (state.value.isEditMode) state.value.imageUrl else null
                    } else {
                        imagePath
                    }

                    if(staffId == -1) {
                        val newStaff = Personal(
                            id = 0,
                            name = newState.name,
                            lastname = newState.lastname,
                            charge = newState.charge,
                            imageUrl = newState.imageUrl
                        )

                        StaffRepository.addStaff(newStaff, imageBytes, AuthRepository.getUserId()!!)
                    }

                    else {
                        val updatedStaff = Personal(
                            id = staffId,
                            name = newState.name,
                            lastname = newState.lastname,
                            charge = newState.charge,
                            imageUrl = urlToSend
                        )

                        StaffRepository.updateStaff(updatedStaff, imageBytes)
                    }

                    _uiEvent.send(StaffFormUiEvent.NavigateToStaff)

                } catch (e: Exception) {

                    _state.update {
                        it.copy(
                            error = "Hubo un error inesperado al intentar registrar al personal"
                        )
                    }

                    println(e.message)
                }
                finally {
                    _state.update { it.copy(isLoading = false) }
                }

            }
        }
        else {
            _state.update {
                newState
            }
        }
    }
}