package com.example.dynasync.ui.feature.createproject

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dynasync.R
import com.example.dynasync.ui.theme.JungleTeal

@Composable
fun CreateProjectForm(
    state: CreateProjectViewState,
    onIntent: (CreateProjectIntent) -> Unit,
    onShowDatePicker: () -> Unit,
    modifier: Modifier = Modifier
) {

    val titleMaxChars = 20
    val objectiveMaxChars = 50
    val descriptionMaxChars = 100

//    val customTextFieldColors = OutlinedTextFieldDefaults.colors(
//        focusedContainerColor = Color.Transparent,
//        focusedBorderColor = Color.Gray,
//        focusedLabelColor = Color.Gray,
//        cursorColor = JungleTeal,
//
//        unfocusedContainerColor = Color.Transparent,
//        unfocusedBorderColor = Color.Gray,
//        unfocusedLabelColor = Color.Gray,
//    )


    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary
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
            OutlinedTextField(
                value = state.title,
                onValueChange = {
                    if(it.length <= titleMaxChars) {
                        onIntent(CreateProjectIntent.TitleChange(it))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.titleError != null,
                label = {
                    Text(text = "Nombre")
                },
                supportingText = {
                    if(state.titleError != null) {
                        Text(
                            text = state.titleError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else {
                        Text(text = "Nombre del proyecto - ${state.title.length}/${titleMaxChars}")
                    }
                },
                maxLines = 1,
                //colors = customTextFieldColors
            )

            OutlinedTextField(
                value = state.objective,
                onValueChange = {
                    if(it.length <= objectiveMaxChars) {
                        onIntent(CreateProjectIntent.ObjectiveChange(it))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.objectiveError != null,
                label = {
                    Text(text = "Objetivo")
                },
                supportingText = {
                    if(state.objectiveError != null) {
                        Text(
                            text = state.objectiveError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else {
                        Text(text = "¿Qué quieres lograr con este negocio? - ${state.objective.length}/${objectiveMaxChars}")
                    }
                },
                maxLines = 2,
               // colors = customTextFieldColors

            )

            OutlinedTextField(
                value = state.description,
                onValueChange = {
                    if(it.length <= descriptionMaxChars)  {
                        onIntent(CreateProjectIntent.DescriptionChange(it))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                isError = state.descriptionError != null,
                label = {
                    Text(text = "Descripción")
                },
                supportingText = {
                    if(state.descriptionError != null) {
                        Text(
                            text = state.descriptionError,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else {
                        Text(text = "Descripción detallada - ${state.description.length}/${descriptionMaxChars}")
                    }
                },
                maxLines = 5,
                //colors = customTextFieldColors
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
                                    onShowDatePicker()
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
                //colors = customTextFieldColors
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
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Crear proyecto",
            style = MaterialTheme.typography.titleMedium
        )
    }
}