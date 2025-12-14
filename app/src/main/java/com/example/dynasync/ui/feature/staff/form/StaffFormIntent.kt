package com.example.dynasync.ui.feature.staff.form

sealed interface StaffFormIntent {
    data class LoadStaff(val staffId: Int) : StaffFormIntent

    data class NameChange(val name: String) : StaffFormIntent
    data class LastNameChange(val lastname: String) : StaffFormIntent
    data class ChargeChange(val charge: String) : StaffFormIntent
    data class ImageUrlChange(val imageUrl: String?) : StaffFormIntent

    data object SubmitForm : StaffFormIntent
}