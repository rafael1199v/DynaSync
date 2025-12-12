package com.example.dynasync.ui.feature.staff

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.data.repository.StaffRepository

@Composable
fun StaffScreen(
    modifier: Modifier = Modifier,
    viewModel: StaffViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    if(state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else if(state.error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.error!!,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
    else {
        StaffScreenContent(
            onIntent = {
                viewModel.onIntent(it)
            },
            state = state,
            modifier = modifier
        )
    }

}



@Composable
fun StaffScreenContent(
    onIntent: (StaffIntent) -> Unit,
    state: StaffViewState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.padding(horizontal = 36.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 40.dp,
            bottom = 80.dp
        )
    ) {
        items(items = state.staff) { staff ->
            StaffCard(
                staff = staff,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StaffScreenPreview() {

    StaffScreenContent(
        onIntent = {},
        state = StaffViewState(
            staff = StaffRepository.staff
        ),
        modifier = Modifier.fillMaxWidth()
    )
}