package com.example.dynasync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.dynasync.data.NavigationBarList
import com.example.dynasync.navigation.AuthenticationDestination
import com.example.dynasync.navigation.AuthenticationGraph
import com.example.dynasync.navigation.MainDestination
import com.example.dynasync.navigation.MainGraph
import com.example.dynasync.ui.factory.LayoutFactoryProvider
import com.example.dynasync.ui.feature.home.HomeScreen
import com.example.dynasync.ui.feature.login.LoginScreen
import com.example.dynasync.ui.feature.payment.PaymentScreen
import com.example.dynasync.ui.feature.projectdetail.ProjectDetailScreen
import com.example.dynasync.ui.theme.DynaSyncTheme
import com.example.dynasync.ui.theme.IcyBlue
import com.example.dynasync.ui.theme.JungleTeal


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DynaSyncTheme {
                App(modifier = Modifier.fillMaxSize())
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    modifier: Modifier = Modifier
) {
    var userIsAuthenticated by remember { mutableStateOf(value = true )}
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val layoutFactory = LayoutFactoryProvider.getFactoryForRoute(navController = navController, entry = navBackStackEntry)
    val layoutConfig = layoutFactory.create()

    val startDestinationGraph = if(userIsAuthenticated) MainGraph else AuthenticationGraph

    Scaffold(
        modifier = modifier,
        topBar = {

            if(layoutConfig.isVisible) {
                TopAppBar(
                    title = {
                        Text(text = layoutConfig.title ?: "DynaSync")
                    },
                    navigationIcon = {
                        layoutConfig.navigationIconId?.let { iconId ->
                            IconButton(
                                onClick = layoutConfig.onNavigationIconClick ?: {}
                            ) {
                                Icon(
                                    painter = painterResource(id = iconId),
                                    contentDescription = "Navigation Icon"
                                )
                            }
                        }
                    },
                    actions = {
                        layoutConfig.actionsList.forEach { actionItem ->
                            IconButton(
                                onClick = actionItem.onClick
                            ) {
                                Icon(
                                    painter = painterResource(id = actionItem.iconId),
                                    contentDescription = actionItem.contentDescription
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = IcyBlue.copy(alpha = 0.2f)
                    )
                )
            }

        },
        bottomBar = {
            if(layoutConfig.isVisible) {
                NavigationBar(
                    containerColor = IcyBlue.copy(alpha = 0.2f)
                ) {

                    val currentDestination = navBackStackEntry?.destination
                    NavigationBarList.items.forEachIndexed { index, item ->

                        val isSelected = currentDestination?.hierarchy?.any { destinationInHierarchy ->
                            destinationInHierarchy.hasRoute(item.destination::class)
                        } == true

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.destination)
                            },
                            label = {
                                Text(text = item.label)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = if(isSelected) item.selectedIconId else item.unselectedIconId),
                                    contentDescription = item.label
                                )
                            }
                        )
                    }
                }
            }

        },
        floatingActionButton = {

            if(layoutConfig.onFloatActionButtonClick != null) {
                FloatingActionButton(
                    containerColor = JungleTeal,
                    onClick = layoutConfig.onFloatActionButtonClick,
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            ),
                            modifier = Modifier.padding(all = 16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = layoutConfig.floatingActionButtonIconId!!),
                                contentDescription = "Float Action Button",
                                tint = Color.White
                            )

                            Text(text = layoutConfig.floatingActionButtonText ?: "", color = Color.White)
                        }
                    },
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestinationGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<AuthenticationGraph>(startDestination = AuthenticationDestination.Login) {
                composable<AuthenticationDestination.Login>{
                    LoginScreen(modifier = Modifier.fillMaxSize(), onLoginSuccess = {
                        navController.navigate(MainDestination.Home) {
                            popUpTo(AuthenticationGraph) {
                                inclusive = true
                            }
                        }
                    })
                }

                composable<AuthenticationDestination.Register> {
                    Text(text = "Registro")
                }
            }

            navigation<MainGraph>(startDestination = MainDestination.Home) {
                composable<MainDestination.Home> {
                    HomeScreen(modifier = Modifier.fillMaxSize(), navController = navController)
                }

                composable<MainDestination.Payment> {
                    PaymentScreen(modifier = Modifier.fillMaxSize())
                }

                composable<MainDestination.Staff> {
                    Text(text = "Personal")
                }


                composable<MainDestination.ProjectDetail> {
                    ProjectDetailScreen()
                }
            }
        }
    }
}