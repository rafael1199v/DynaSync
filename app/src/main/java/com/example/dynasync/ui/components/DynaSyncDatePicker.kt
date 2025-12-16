package com.example.dynasync.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier


@Composable
fun DynaSyncDatePicker(
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit,
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier
){

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmButtonClick()
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissButtonClick()
                }
            ) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }

}
