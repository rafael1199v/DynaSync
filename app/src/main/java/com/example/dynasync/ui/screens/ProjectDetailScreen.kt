package com.example.dynasync.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.viewmodels.ProjectDetailViewModel

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    Text(text = "${state.project.toString()}")
}