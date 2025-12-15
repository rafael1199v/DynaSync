package com.example.dynasync.ui.feature.projectdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.dynasync.data.repository.AuthRepository
import com.example.dynasync.data.repository.ProjectRepository
import com.example.dynasync.data.repository.ProjectRepository.getProjectById
import com.example.dynasync.data.repository.StaffRepository
import com.example.dynasync.data.repository.TaskRepository
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Task
import com.example.dynasync.navigation.MainDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class ProjectDetailViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<MainDestination.ProjectDetail>()

    private val _state = MutableStateFlow(value = ProjectDetailState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<ProjectDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val projectId = args.projectId

    init {
        _state.update {
            it.copy(isLoading = true)
        }

        onIntent(ProjectDetailIntent.LoadProject(projectId))
        loadStaff()
    }

    fun onIntent(intent: ProjectDetailIntent) {
        when(intent) {
            is ProjectDetailIntent.LoadProject -> {
                getProjectById(intent.projectId)
            }
            is ProjectDetailIntent.DeleteProject -> {
                deleteProject(intent.projectId)
            }
            is ProjectDetailIntent.EditProject -> {
                editProject(intent.projectId)
            }
            is ProjectDetailIntent.DeleteTask -> {
                deleteTask(intent.taskId)
            }
            is ProjectDetailIntent.ToggleTask -> {
                onToggleTask(intent.taskId)
            }
            is ProjectDetailIntent.CreateTask -> {
                addTask(intent.title, intent.staffId, intent.finishDate)
            }
            is ProjectDetailIntent.UpdateTask -> {
                updateTask(intent.taskId, intent.title, intent.staffId, intent.finishDate)
            }
        }
    }

    private fun getProjectById(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val project = ProjectRepository.getProjectById(projectId = projectId)
            val sortedProject = project?.let {
                it.copy(
                    tasks = it.tasks.sortedWith(
                        compareBy<Task> { task -> task.isCompleted }
                            .thenBy { task -> task.finishDate }
                    )
                )
            }

            _state.update {
                it.copy(
                    project = sortedProject,
                    isLoading = false
                )
            }
        }
    }

    private fun onToggleTask(taskId: Int) {
        viewModelScope.launch {
            TaskRepository.toggleTask(taskId)
        }
    }

    private fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            ProjectRepository.deleteProject(projectId = projectId)
            _uiEvent.send(ProjectDetailUiEvent.NavigateToHome)
        }

    }

    private fun editProject(projectId: Int) {
        viewModelScope.launch {
            _uiEvent.send(ProjectDetailUiEvent.NavigateToEditProject(projectId = projectId))
        }
    }

    private fun addTask(title: String, staffId: Int?, finishDate: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val staff = _state.value.staffList.find { it.id == staffId }

            val newTask = Task(
                id = 0,
                title = title,
                isCompleted = false,
                personal = staff,
                finishDate = LocalDate.parse(finishDate)
            )

            TaskRepository.addTask(newTask, projectId)

            onIntent(ProjectDetailIntent.LoadProject(projectId))
        }
    }

    private fun updateTask(taskId: Int, title: String, staffId: Int?, finishDate: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val staff = _state.value.staffList.find { it.id == staffId }

            val task = Task(
                id = taskId,
                title = title,
                isCompleted = false,
                personal = staff,
                finishDate = LocalDate.parse(finishDate)
            )

            TaskRepository.updateTask(task)

            onIntent(ProjectDetailIntent.LoadProject(projectId))
        }
    }

    private fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            TaskRepository.deleteTask(taskId = taskId)
            onIntent(ProjectDetailIntent.LoadProject(projectId))
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun loadStaff() {
        viewModelScope.launch {
            val staffList = StaffRepository.getStaff(AuthRepository.getUserId()!!)
            _state.update { it.copy(staffList = staffList) }

        }
    }
}