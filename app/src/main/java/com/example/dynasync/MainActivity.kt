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
import com.example.dynasync.ui.App
import com.example.dynasync.ui.factory.LayoutFactoryProvider
import com.example.dynasync.ui.feature.createproject.CreateProjectScreen
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
