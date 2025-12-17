package com.example.dynasync.ui.feature.createproject

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dynasync.data.repository.AuthRepository
import com.example.dynasync.data.repository.ProjectRepository
import com.example.dynasync.domain.model.Project
import com.example.dynasync.navigation.MainDestination
import com.example.dynasync.ui.feature.createproject.validators.ProjectFormValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.io.File

class CreateProjectViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val args = savedStateHandle.toRoute<MainDestination.CreateProject>()
    val projectId = args.projectId
    private val isEditMode = projectId != -1
    private var editableProject : Project? = null

    private val _state = MutableStateFlow(value = CreateProjectViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<CreateProjectUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val projectFormValidator = ProjectFormValidator()

    init {
        if(isEditMode) {
            _state.update {
                it.copy(isEditMode = true)
            }
            onIntent(CreateProjectIntent.LoadProject(projectId))
        }
    }


    fun onIntent(intent: CreateProjectIntent) {
        when(intent) {
            is CreateProjectIntent.TitleChange -> {
                onChangeTitle(intent.title)
            }
            is CreateProjectIntent.ObjectiveChange -> {
                onChangeObjective(intent.objective)
            }
            is CreateProjectIntent.DescriptionChange -> {
                onChangeDescription(intent.description)
            }
            is CreateProjectIntent.FinishDateChange -> {
                onFinishDateChange(intent.finishDate)
            }
            is CreateProjectIntent.ImageUrlChange -> {
                onImageChange(intent.imageUrl)
            }
            is CreateProjectIntent.SubmitProjectForm -> {
                submitForm()
            }
            is CreateProjectIntent.LoadProject -> {
                loadProjectData()
            }
            is CreateProjectIntent.CleanError -> {
                onCleanError()
            }
        }
    }

    private fun onChangeTitle(title: String) {
        _state.update {
            it.copy(title = title, titleError = null)
        }
    }


    private fun onChangeObjective(objective: String) {
        _state.update {
            it.copy(objective = objective, objectiveError = null)
        }
    }

    private fun onChangeDescription(description: String) {
        _state.update {
            it.copy(description = description, descriptionError = null)
        }
    }

    private fun onFinishDateChange(finishDate: String) {
        _state.update {
            it.copy(finishDate = finishDate, finishDateError = null)
        }
    }

    private fun onImageChange(imageUrl: String) {
        _state.update {
            it.copy(imageUrl = imageUrl)
        }
    }

    private fun loadProjectData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val project = ProjectRepository.getProjectById(projectId)

            editableProject = project

            _state.update {
                it.copy(
                    title = project?.title ?: "",
                    description = project?.description ?: "",
                    objective = project?.objective ?: "",
                    finishDate = project?.finishDate?.toString() ?: "",
                    imageUrl = project?.imageUrl ?: "",
                    isLoading = false
                )
            }
        }
    }

    private fun onCleanError() {
        _state.update {
            it.copy(error = null)
        }
    }


    private fun submitForm() {
        val newState = projectFormValidator.validate(state.value)


        if(projectFormValidator.isValid(newState)) {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                val userId = AuthRepository.getUserId() ?: return@launch

                try {
                    val imagePath = newState.imageUrl
                    var imageBytes: ByteArray? = null

                    val isRemoteUrl = imagePath.startsWith("http")
                    val isLocalFile = imagePath.isNotEmpty() && !isRemoteUrl

                    if (isLocalFile) {
                        imageBytes = File(imagePath).readBytes()
                    }

                    val urlToSend = if (isLocalFile) {
                        if (isEditMode) editableProject?.imageUrl else null
                    } else {
                        imagePath
                    }

                    if(isEditMode) {
                        val updatedProject = editableProject!!.copy(
                            title = newState.title,
                            objective = newState.objective,
                            description = newState.description,
                            finishDate = LocalDate.parse(newState.finishDate),
                            imageUrl = urlToSend
                        )

                        ProjectRepository.updateProject(updatedProject, newImageBytes = imageBytes)
                    }
                    else {
                        val newProject = Project(
                            id = 0,
                            title = newState.title,
                            objective = newState.objective,
                            description = newState.description,
                            finishDate = LocalDate.parse(newState.finishDate),
                            imageUrl = null,
                            tasks = emptyList()
                        )
                        ProjectRepository.addProject(newProject, imageBytes = imageBytes, userId = userId)
                    }

                    _uiEvent.send(CreateProjectUiEvent.NavigateToHome)
                }
                catch (e: Exception) {
                    Log.e("debug", "Hubo un error al crear el proyecto. ${e}")
                    _state.update { it.copy(error = e.message) }
                }
                finally {
                    _state.update { it.copy(isLoading = false) }
                }

                _state.update { it.copy(isLoading = false) }

                _uiEvent.send(CreateProjectUiEvent.NavigateToHome)
            }

        }
        else {
            _state.update {
                newState
            }
        }
    }

}