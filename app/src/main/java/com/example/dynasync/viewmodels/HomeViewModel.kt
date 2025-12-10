package com.example.dynasync.viewmodels
import androidx.lifecycle.ViewModel
import com.example.dynasync.domain.Project
import com.example.dynasync.ui.states.HomeViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(value = HomeViewState())
    val state = _state.asStateFlow()

    init {
        _state.value = HomeViewState(
            projectsInProcess = 2,
            pendingTasks = 3,
            projects = listOf(
                Project(
                    id = 1,
                    title = "DynaSync",
                    objective = "Mejorar la productividad de las personas",
                    description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                    imageUrl = "https://images.unsplash.com/photo-1761839257864-c6ccab7238de?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    tasks = emptyList()
                ),
                Project(
                    id = 2,
                    title = "Segundo proyecto",
                    objective = "Mejorar la productividad de las personas",
                    description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                    imageUrl = "https://images.unsplash.com/photo-1761839256791-6a93f89fb8b0?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                    tasks = emptyList()
                )
            )
        )

    }
}