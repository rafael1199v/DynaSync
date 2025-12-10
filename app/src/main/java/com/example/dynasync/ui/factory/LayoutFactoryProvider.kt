package com.example.dynasync.ui.factory

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.dynasync.R
import com.example.dynasync.navigation.MainDestination

object LayoutFactoryProvider {
    fun getFactoryForRoute(
        navController: NavController,
        entry: NavBackStackEntry?
    ): LayoutFactory {

        if(entry == null)
            return EmptyLayoutFactory()

        val destination = entry.destination

        return when {
            destination.hasRoute<MainDestination.Home>() -> {
                HomeLayoutFactory(
                    onFloatActionButtonClick = {},
                    onNavigationIconClick = {},
                    listActions = listOf(
                        ActionItem(
                            iconId = R.drawable.outline_account_circle_24,
                            contentDescription = "Profile",
                            onClick = { }
                        )
                    )
                )
            }

            destination.hasRoute<MainDestination.Payment>() -> {
                PaymentLayoutFactory(
                    onNavigationIconClick = { navController.popBackStack() },
                    onFloatActionButtonClick = {},
                    listActions = listOf(
                        ActionItem(
                            iconId = R.drawable.outline_account_circle_24,
                            contentDescription = "Profile",
                            onClick = {}
                        )
                    )
                )
            }

            else -> EmptyLayoutFactory()
        }
    }

}