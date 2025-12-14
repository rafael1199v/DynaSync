package com.example.dynasync.ui.factory

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
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
                    onFloatActionButtonClick = null,
                    onNavigationIconClick = {},
                    listActions = listOf(
                        ActionItem(
                            iconId = R.drawable.outline_account_circle_24,
                            contentDescription = "Profile",
                            onClick = {}
                        )
                    )
                )
            }

            destination.hasRoute<MainDestination.Payment>() -> {
                PaymentLayoutFactory(
                    onNavigationIconClick = { navController.popBackStack() },
                    onFloatActionButtonClick = null,
                    listActions = listOf(
                        ActionItem(
                            iconId = R.drawable.outline_account_circle_24,
                            contentDescription = "Profile",
                            onClick = {}
                        )
                    )
                )
            }

            destination.hasRoute<MainDestination.ProjectDetail>() -> {
                val args = entry.toRoute<MainDestination.ProjectDetail>()

                ProjectDetailLayoutFactory(
                    onNavigationIconClick = { navController.popBackStack() },
                    onFloatActionButtonClick = null,
                    listActions = listOf(
                        ActionItem(
                            iconId = R.drawable.outline_account_circle_24,
                            contentDescription = "Profile",
                            onClick = {}
                        )
                    )
                )
            }

            destination.hasRoute<MainDestination.CreateProject>() -> {

                val args = entry.toRoute<MainDestination.CreateProject>()

                if(args.projectId == -1) {
                    CreateProjectLayoutFactory(
                        onNavigationIconClick = { navController.popBackStack() },
                        onFloatActionButtonClick = null,
                        listActions = listOf(
                            ActionItem(
                                iconId = R.drawable.outline_account_circle_24,
                                contentDescription = "Profile",
                                onClick = {}
                            )
                        )
                    )
                }
                else {
                    UpdateProjectLayoutFactory(
                        onNavigationIconClick = { navController.popBackStack() },
                        onFloatActionButtonClick = null,
                        listActions = listOf(
                            ActionItem(
                                iconId = R.drawable.outline_account_circle_24,
                                contentDescription = "Profile",
                                onClick = {}
                            )
                        )
                    )
                }

            }


            destination.hasRoute<MainDestination.Staff>() -> {
                StaffLayoutFactory(
                    onNavigationIconClick = { navController.popBackStack() },
                    onFloatActionButtonClick = null,
                    listActions = listOf(
                        ActionItem(
                            iconId = R.drawable.outline_account_circle_24,
                            contentDescription = "Profile",
                            onClick = {}
                        )
                    )
                )
            }

            destination.hasRoute<MainDestination.StaffForm>() -> {
                val args = entry.toRoute<MainDestination.StaffForm>()

                if(args.staffId == -1) {
                    StaffCreateLayoutFactory(
                        onNavigationIconClick = { navController.popBackStack() },
                        onFloatActionButtonClick = null,
                        listActions = listOf(
                            ActionItem(
                                iconId = R.drawable.outline_account_circle_24,
                                contentDescription = "Profile",
                                onClick = {}
                            )
                        )
                    )
                }
                else {
                    StaffEditLayoutFactory(
                        onNavigationIconClick = { navController.popBackStack() },
                        onFloatActionButtonClick = null,
                        listActions = listOf(
                            ActionItem(
                                iconId = R.drawable.outline_account_circle_24,
                                contentDescription = "Profile",
                                onClick = {}
                            )
                        )
                    )
                }
            }

            destination.hasRoute<MainDestination.PaymentForm>() -> {
                val args = entry.toRoute<MainDestination.PaymentForm>()

                if(args.paymentId == -1) {
                    PaymentCreateLayoutFactory(
                        onNavigationIconClick = { navController.popBackStack() },
                        onFloatActionButtonClick = null,
                        listActions = listOf(
                            ActionItem(
                                iconId = R.drawable.outline_account_circle_24,
                                contentDescription = "Profile",
                                onClick = {}
                            )
                        )
                    )
                }
                else {
                    PaymentEditLayoutFactory(
                        onNavigationIconClick = { navController.popBackStack() },
                        onFloatActionButtonClick = null,
                        listActions = listOf(
                            ActionItem(
                                iconId = R.drawable.outline_account_circle_24,
                                contentDescription = "Profile",
                                onClick = {}
                            )
                        )
                    )
                }
            }

            else -> EmptyLayoutFactory()
        }
    }

}