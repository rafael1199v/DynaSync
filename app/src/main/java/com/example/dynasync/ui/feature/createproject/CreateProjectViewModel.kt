package com.example.dynasync.ui.feature.createproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynasync.data.repository.ProjectRepository
import com.example.dynasync.domain.model.Project
import com.example.dynasync.ui.feature.projectdetail.ProjectDetailState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class CreateProjectViewModel: ViewModel() {

    private val _state = MutableStateFlow(value = CreateProjectViewState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<CreateProjectUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val projectFormValidator = ProjectFormValidator()

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
        }
    }

    private fun onChangeTitle(title: String) {
        _state.update {
            it.copy(title = title)
        }
    }


    private fun onChangeObjective(objective: String) {
        _state.update {
            it.copy(objective = objective)
        }
    }

    private fun onChangeDescription(description: String) {
        _state.update {
            it.copy(description = description)
        }
    }

    private fun onFinishDateChange(finishDate: String) {
        _state.update {
            it.copy(finishDate = finishDate)
        }
    }

    private fun onImageChange(imageUrl: String) {
        _state.update {
            it.copy(imageUrl = imageUrl)
        }
    }


    private fun submitForm() {
        val newState = projectFormValidator.validate(state.value)


        if(projectFormValidator.isValid(newState)) {
            viewModelScope.launch {
                println(newState)

                val (day, month, year) = newState.finishDate.split("/")
                val isoDate = "$year-$month-$day"

                val newProject = Project(
                    id = -1,
                    title = newState.title,
                    objective = newState.objective,
                    description = newState.description,
                    finishDate = LocalDate.parse(isoDate),
                    imageUrl = newState.imageUrl.ifEmpty { null },
                    tasks = emptyList()
                )

                ProjectRepository.addProject(newProject)

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