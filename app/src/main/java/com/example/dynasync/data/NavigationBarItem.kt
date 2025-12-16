package com.example.dynasync.data

import com.example.dynasync.R
import com.example.dynasync.navigation.MainDestination

data class NavigationBarItem(
    val label: String,
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val destination: MainDestination
)


object NavigationBarList {
    val items: List<NavigationBarItem> = listOf(
        NavigationBarItem(
            label = "Home",
            selectedIconId = R.drawable.baseline_home_24,
            unselectedIconId = R.drawable.baseline_home_24,
            destination = MainDestination.Home
        ),
        NavigationBarItem(
            label = "Pagos",
            selectedIconId = R.drawable.baseline_monetization_on_24,
            unselectedIconId = R.drawable.baseline_monetization_on_24,
            destination = MainDestination.Payment
        ),
        NavigationBarItem(
            label = "Personal",
            selectedIconId = R.drawable.outline_productivity_24,
            unselectedIconId = R.drawable.outline_productivity_24,
            destination = MainDestination.Staff
        )
    )
}