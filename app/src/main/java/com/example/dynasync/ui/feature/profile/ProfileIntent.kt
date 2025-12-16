package com.example.dynasync.ui.feature.profile

import com.example.dynasync.ui.theme.AppThemeMode

sealed interface ProfileIntent {
    data object LoadUser : ProfileIntent
    data object Logout: ProfileIntent

    data class ChangeTheme(val newTheme: AppThemeMode) : ProfileIntent
}