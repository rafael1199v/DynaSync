package com.example.dynasync.ui.feature.staff

sealed interface StaffUiEvent {
    data class NavigateToEditForm(val staffId: Int): StaffUiEvent
}