package com.example.dynasync.ui.feature.staff

sealed interface StaffIntent {
    data object LoadStaff : StaffIntent

    data class UpdateStaff(val staffId: Int) : StaffIntent

    data class DeleteStaff(val staffId: Int) : StaffIntent


}