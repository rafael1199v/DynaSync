package com.example.dynasync.ui.feature.staff

sealed interface StaffIntent {
    data object LoadStaff : StaffIntent
}