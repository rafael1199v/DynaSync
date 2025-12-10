package com.example.dynasync.navigation

import androidx.core.widget.AutoScrollHelper
import kotlinx.serialization.Serializable


@Serializable
data object AuthenticationGraph

@Serializable
data object MainGraph


sealed interface MainDestination {
    @Serializable
    data object Home : MainDestination

    @Serializable
    data object Payment : MainDestination

    @Serializable
    data object Staff : MainDestination
}


sealed interface AuthenticationDestination {
    @Serializable
    data object Login : AuthenticationDestination

    @Serializable
    data object Register : AuthenticationDestination
}
