package com.example.dynasync.ui.feature.staff

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.R
import com.example.dynasync.data.repository.StaffRepository
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.ui.components.DynaSyncFloatingActionButton
import com.example.dynasync.ui.feature.projectdetail.ProjectDetailIntent
import com.example.dynasync.ui.feature.staff.form.StaffFormUiEvent

@Composable
fun StaffScreen(
    onCreateStaff: () -> Unit,
    onEditStaff: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StaffViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is StaffUiEvent.NavigateToEditForm -> {
                    onEditStaff(event.staffId)
                }
            }
        }
    }


    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        floatingActionButton = {
            DynaSyncFloatingActionButton(
                onClick = onCreateStaff,
                text = "Personal",
                iconId = R.drawable.baseline_add_24,
                contentDescription = "Floating Action Button Staff"
            )
        }
    ){ contentPadding ->
        if(state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary )
            }
        }
        else if(state.error != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(contentPadding),
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

}



@Composable
fun StaffScreenContent(
    onIntent: (StaffIntent) -> Unit,
    state: StaffViewState,
    modifier: Modifier = Modifier
) {

    var staffToDelete by remember { mutableStateOf<Personal?>(value = null) }


    if(state.staff.isEmpty()) {
        Column(
            modifier.fillMaxSize().padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.dynasync_no_staff_light),
                contentDescription = "No staff image",
                modifier = Modifier.fillMaxWidth().height(294.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No tienes personal registrado. Agrega a una persona!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
    else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 26.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = 40.dp,
                bottom = 80.dp
            )
        ) {
            items(items = state.staff) { staff ->
                StaffCard(
                    staff = staff,
                    modifier = Modifier.fillMaxWidth(),
                    onDeleteStaff = {
                        //onIntent(StaffIntent.DeleteStaff(staff.id))
                        staffToDelete = staff
                    },
                    onUpdateStaff = {
                        onIntent(StaffIntent.UpdateStaff(staff.id))
                    }
                )
            }
        }

    }


    if (staffToDelete != null) {
        AlertDialog(
            onDismissRequest = { staffToDelete = null },
            title = { Text(text = "Eliminar personal") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "¿Deseas eliminar de la lista de personal a '${staffToDelete?.name} ${staffToDelete?.lastname}'?")
                    Text(text = " Ten en cuenta que las tareas asignadas a este empleado se quedarán sin responsable.")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onIntent(StaffIntent.DeleteStaff(staffToDelete!!.id))
                        staffToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = "Eliminar"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { staffToDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StaffScreenPreview() {

    StaffScreenContent(
        onIntent = {},
        state = StaffViewState(

        ),
        modifier = Modifier.fillMaxWidth()
    )
}