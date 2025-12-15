package com.example.dynasync.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.dynasync.data.NavigationBarList
import com.example.dynasync.data.repository.AuthRepository
import com.example.dynasync.data.supabase.SupabaseClientObject
import com.example.dynasync.navigation.AuthenticationDestination
import com.example.dynasync.navigation.AuthenticationGraph
import com.example.dynasync.navigation.MainDestination
import com.example.dynasync.navigation.MainGraph
import com.example.dynasync.ui.factory.LayoutFactoryProvider
import com.example.dynasync.ui.feature.createproject.CreateProjectScreen
import com.example.dynasync.ui.feature.home.HomeScreen
import com.example.dynasync.ui.feature.login.LoginScreen
import com.example.dynasync.ui.feature.payment.PaymentScreen
import com.example.dynasync.ui.feature.payment.form.PaymentFormScreen
import com.example.dynasync.ui.feature.profile.ProfileScreen
import com.example.dynasync.ui.feature.projectdetail.ProjectDetailScreen
import com.example.dynasync.ui.feature.register.RegisterScreen
import com.example.dynasync.ui.feature.staff.StaffScreen
import com.example.dynasync.ui.feature.staff.form.StaffFormScreen
import com.example.dynasync.ui.theme.IcyBlue
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()

    val sessionStatus by SupabaseClientObject.client.auth.sessionStatus.collectAsState(
        initial = SessionStatus.Initializing
    )

    var isReady by remember { mutableStateOf(false) }
    var startDestinationGraph by remember { mutableStateOf<Any>(AuthenticationGraph) }


    LaunchedEffect(sessionStatus) {
        when (sessionStatus) {
            is SessionStatus.Initializing -> {}
            is SessionStatus.Authenticated -> {
                if (!isReady) {
                    startDestinationGraph = MainGraph
                    isReady = true
                }
            }
            is SessionStatus.NotAuthenticated -> {
                if (!isReady) {
                    startDestinationGraph = AuthenticationGraph
                    isReady = true
                } else {
                    navController.navigate(AuthenticationGraph) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            else -> {
                if (!isReady) {
                    startDestinationGraph = AuthenticationGraph
                    isReady = true
                }
            }
        }
    }

    if (!isReady) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }


    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val layoutFactory = LayoutFactoryProvider.getFactoryForRoute(navController = navController, entry = navBackStackEntry)
    val layoutConfig = layoutFactory.create()

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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestinationGraph,
            modifier = Modifier.padding(innerPadding)
        ) {
            navigation<AuthenticationGraph>(startDestination = AuthenticationDestination.Login) {
                composable<AuthenticationDestination.Login>{
                    LoginScreen(
                        modifier = Modifier.fillMaxSize(),
                        onLoginSuccess = {
                            navController.navigate(MainDestination.Home) {
                                popUpTo(AuthenticationGraph) {
                                    inclusive = true
                                }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(AuthenticationDestination.Register)
                        }
                    )
                }

                composable<AuthenticationDestination.Register> {
                    RegisterScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            navigation<MainGraph>(startDestination = MainDestination.Home) {
                composable<MainDestination.Home> {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize(),
                        onProjectClick = { projectId ->
                            navController.navigate(MainDestination.ProjectDetail(projectId))
                        },
                        onCreateProject = {
                            navController.navigate(MainDestination.CreateProject())
                        }
                    )
                }

                composable<MainDestination.Payment> {
                    PaymentScreen(
                        modifier = Modifier.fillMaxSize(),
                        onCreatePayment = {
                            navController.navigate(MainDestination.PaymentForm())
                        },
                        onEditPayment = { paymentId ->
                            navController.navigate(MainDestination.PaymentForm(paymentId))
                        }
                    )
                }

                composable<MainDestination.Staff> {
                    StaffScreen(
                        modifier = Modifier.fillMaxSize(),
                        onCreateStaff = {
                            navController.navigate(MainDestination.StaffForm())
                        },
                        onEditStaff = { staffId ->
                            navController.navigate(MainDestination.StaffForm(staffId))
                        }
                    )
                }

                composable<MainDestination.ProjectDetail> {
                    ProjectDetailScreen(
                        modifier = Modifier.fillMaxSize(),
                        onDeleteProjectSuccess = {
                            navController.navigate(MainDestination.Home)
                        },
                        onClickEditProject = { projectId ->
                            navController.navigate(MainDestination.CreateProject(projectId))
                        }
                    )
                }

                composable<MainDestination.CreateProject> {
                    CreateProjectScreen(
                        onSubmitFormSuccess = {
                            navController.navigate(MainDestination.Home)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable<MainDestination.StaffForm> {
                    StaffFormScreen(
                        onSubmitFormSuccess = {
                            navController.navigate(MainDestination.Staff)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable<MainDestination.PaymentForm> {
                    PaymentFormScreen(
                        modifier = Modifier.fillMaxWidth(),
                        onSubmitFormSuccess = {
                            navController.navigate(MainDestination.Payment)
                        }
                    )
                }

                composable<MainDestination.Profile> {
                    ProfileScreen(
                        modifier = Modifier.fillMaxSize(),
                        onLogout = {
                            CoroutineScope(Dispatchers.IO).launch {
                                AuthRepository.signOut()
                            }
                        }
                    )
                }
            }
        }
    }
}