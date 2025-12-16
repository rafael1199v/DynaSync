package com.example.dynasync.ui.feature.projectdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dynasync.R
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Task
import com.example.dynasync.ui.components.DynaSyncDatePicker
import com.example.dynasync.ui.components.DynaSyncTextField
import com.example.dynasync.ui.theme.JungleTeal
import com.example.dynasync.utils.convertMillisToDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneId
import java.time.LocalDate as JavaLocalDate
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormBottomSheet(
    staffList: List<Personal>,
    onDismissRequest: () -> Unit,
    onSaveClick: (String, LocalDate, Personal?) -> Unit,
    initialTask: Task? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val todayMillis = JavaLocalDate.now()
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli()

                return utcTimeMillis >= todayMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= today.year
            }
        }
    )

    var title by remember { mutableStateOf(initialTask?.title ?: "") }
    var isTitleError by remember { mutableStateOf(false) }
    val maxTitleLength: Int = 20

    var showStaffDialog by remember { mutableStateOf(false) }

    var finishDate by remember {
        mutableStateOf(initialTask?.finishDate ?: today)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedPersonal by remember { mutableStateOf(initialTask?.personal) }


    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (initialTask == null) "Nueva Tarea" else "Editar Tarea",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            DynaSyncTextField(
                value = title,
                onValueChange = {
                    if(it.length <= maxTitleLength) {
                        title = it
                    }
                },
                label = "Titulo",
                errorMessage = if (isTitleError) "El titulo es requerido" else null,
                supportingText = "Titulo de la tarea",
                charCount = title.length,
                maxChars = maxTitleLength,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = finishDate.toString(),
                onValueChange = { },
                readOnly = true,
                label = { Text("Fecha de finalización") },
                trailingIcon = {
                    Icon(painterResource(id = R.drawable.outline_calendar_today_24), "Calendario")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            if(showDatePicker){
                DynaSyncDatePicker(
                    onDismissRequest = { showDatePicker = false },
                    onConfirmButtonClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formattedDate = convertMillisToDate(millis)
                            finishDate = LocalDate.parse(formattedDate)
                            showDatePicker = false
                        }
                    },
                    onDismissButtonClick = {
                        showDatePicker = false
                    },
                    datePickerState = datePickerState,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = selectedPersonal?.let { "${it.name} ${it.lastname}" } ?: "",
                onValueChange = {}, // No hace nada porque es readOnly
                readOnly = true,
                label = { Text("Asignar a...") },
                placeholder = { Text("Sin asignar") },
                trailingIcon = {
                    // Si hay alguien seleccionado, mostramos una X para borrar rápido
                    if (selectedPersonal != null) {
                        IconButton(onClick = { selectedPersonal = null }) {
                            Icon(painter = painterResource(R.drawable.outline_delete_24), contentDescription = "Borrar")
                        }
                    } else {
                        Icon(painterResource(R.drawable.outline_person_24), "Buscar")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showStaffDialog = true },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if(title.isEmpty()) {
                        isTitleError = true
                    }
                    else {
                        isTitleError = false
                        onSaveClick(title, finishDate, selectedPersonal)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (initialTask == null) "Crear Tarea" else "Guardar Cambios")
            }
        }
    }

    if (showStaffDialog) {
        StaffSelectionDialog(
            staffList = staffList,
            onDismissRequest = { showStaffDialog = false },
            onPersonalSelected = { personal ->
                selectedPersonal = personal
                showStaffDialog = false
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TaskBottomSheetPreview() {
    TaskFormBottomSheet(
        staffList = emptyList(),
        onDismissRequest = {},
        onSaveClick = { a, b, c ->

        },
        initialTask = null
    )
}