package com.example.dynasync.ui.feature.home

sealed interface HomeIntent {
    data object LoadProjects : HomeIntent
}