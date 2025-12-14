package com.example.dynasync.ui.feature.profile

sealed interface ProfileIntent {
    data object LoadUser : ProfileIntent
    data object Logout: ProfileIntent
}