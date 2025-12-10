package com.example.dynasync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.dynasync.data.NavigationBarList
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


@Composable
fun App(
    modifier: Modifier = Modifier
) {
    var selectedNavbarItemId by remember { mutableStateOf(value = 0) }

    Scaffold(
        modifier = modifier,
        topBar = {

        },
        bottomBar = {
            NavigationBar(
                containerColor = IcyBlue.copy(alpha = 0.2f)
            ) {
                NavigationBarList.items.forEachIndexed { index, item ->

                    val isSelected = index == selectedNavbarItemId
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedNavbarItemId = index
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
        },
        floatingActionButton = {

        }
    ) {

    }
}