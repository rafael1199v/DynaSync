package com.example.dynasync.ui.feature.createproject

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.R
import com.example.dynasync.ui.components.DynaSyncDatePicker
import com.example.dynasync.ui.components.DynaSyncTextField
import com.example.dynasync.ui.theme.JungleTeal
import com.example.dynasync.utils.convertMillisToDate

@Composable
fun CreateProjectScreen(
    modifier: Modifier = Modifier,
    viewModel: CreateProjectViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    CreateProjectScreenContent(
        modifier = modifier,
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent = intent)
        }
    )
}

@Composable
fun CreateProjectScreenContent(
    modifier: Modifier = Modifier,
    state: CreateProjectViewState,
    onIntent: (CreateProjectIntent) -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableYear(year: Int): Boolean {
                return year >= 2024
            }
        }
    )

    val titleMaxChars = 20
    val objectiveMaxChars = 50
    val descriptionMaxChars = 100




    Column(
        modifier = modifier.
            padding(start = 26.dp, end = 26.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Inicia un nuevo negocio",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Cada gran proyecto empezó con una simple decisión: atreverte a dar el primer paso incluso antes de sentirte listo.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = JungleTeal
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_photo_24),
                    contentDescription = "Upload image"
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Subir imagen")
            }


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DynaSyncTextField(
                    value = state.title,
                    onValueChange = {
                        if(it.length <= titleMaxChars) {
                            onIntent(CreateProjectIntent.TitleChange(it))
                        }
                    },
                    label = "Nombre",
                    errorMessage = state.titleError,
                    supportingText = "Nombre del proyecto",
                    charCount = state.title.length,
                    maxChars = titleMaxChars,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                DynaSyncTextField(
                    value = state.objective,
                    onValueChange = {
                        if(it.length <= objectiveMaxChars) {
                            onIntent(CreateProjectIntent.ObjectiveChange(it))
                        }
                    },
                    label = "Objetivo",
                    errorMessage = state.objectiveError,
                    supportingText = "¿Qué quieres lograr con este negocio?",
                    charCount = state.objective.length,
                    maxChars = objectiveMaxChars,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                )


                DynaSyncTextField(
                    value = state.description,
                    onValueChange = {
                        if(it.length <= descriptionMaxChars)  {
                            onIntent(CreateProjectIntent.DescriptionChange(it))
                        }
                    },
                    label = "Descripción",
                    errorMessage = state.descriptionError,
                    supportingText = "Descripción detallada",
                    charCount = state.description.length,
                    maxChars = descriptionMaxChars,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )

                OutlinedTextField(
                    value = state.finishDate,
                    onValueChange = {
                        onIntent(CreateProjectIntent.FinishDateChange(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    isError = state.finishDateError != null,
                    readOnly = true,
                    label = { Text("Finalización") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_calendar_today_24),
                            contentDescription = "Select date"
                        )
                    },
                    interactionSource = remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showDatePicker = true
                                    }
                                }
                            }
                    },
                    supportingText = {
                        if(state.finishDateError != null) {
                            Text(
                                text = state.finishDateError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        else {
                            Text(text = "Fecha estimada de finalización")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        cursorColor = JungleTeal,

                        unfocusedContainerColor = Color.Transparent,
                        unfocusedBorderColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray,
                    )
                )

            }
        }

        Button(
            onClick = {
                onIntent(CreateProjectIntent.SubmitProjectForm)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = JungleTeal
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Crear proyecto",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showDatePicker) {

        DynaSyncDatePicker(
            onDismissRequest = { showDatePicker = false },
            onConfirmButtonClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val formattedDate = convertMillisToDate(millis)
                    onIntent(CreateProjectIntent.FinishDateChange(formattedDate))
                }
                showDatePicker = false
            },
            onDismissButtonClick = { showDatePicker = false },
            datePickerState = datePickerState
        )
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateProjectScreenPreview() {
    CreateProjectScreenContent(
        state = CreateProjectViewState(),
        onIntent = {},
        modifier = Modifier.fillMaxSize()
    )
}



