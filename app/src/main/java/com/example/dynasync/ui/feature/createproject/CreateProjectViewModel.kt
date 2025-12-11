package com.example.dynasync.ui.feature.createproject

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreateProjectViewModel: ViewModel() {

    private val _state = MutableStateFlow(value = CreateProjectViewState())
    val state = _state.asStateFlow()

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
            //Logica de crear projecto
        }
        else {
            _state.update {
                newState
            }
        }
    }

}