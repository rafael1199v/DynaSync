package com.example.dynasync.ui.feature.home

sealed interface HomeIntent {
    data object LoadProjects : HomeIntent
    data object LoadUser : HomeIntent
    data object LoadDashboardData : HomeIntent
    data object CleanError: HomeIntent
}