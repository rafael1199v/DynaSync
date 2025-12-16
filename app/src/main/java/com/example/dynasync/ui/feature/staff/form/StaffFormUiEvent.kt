package com.example.dynasync.ui.feature.staff.form

sealed interface StaffFormUiEvent {
    data object NavigateToStaff: StaffFormUiEvent
}