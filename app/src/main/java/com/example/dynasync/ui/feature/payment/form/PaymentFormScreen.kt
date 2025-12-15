package com.example.dynasync.ui.feature.payment.form

import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynasync.R
import com.example.dynasync.domain.model.PaymentType
import com.example.dynasync.ui.components.DynaSyncTextField
import com.example.dynasync.ui.theme.JungleTeal
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentFormScreen(
    onSubmitFormSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PaymentFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is PaymentFormUiEvent.NavigateToPayment -> {
                    onSubmitFormSuccess()
                }
            }
        }
    }

    // --- Lógica de Date Picker ---
    if (state.showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { viewModel.onIntent(PaymentFormIntent.DismissDatePicker) },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convertir millis a LocalDate usando kotlinx-datetime
                        val instant = Instant.fromEpochMilliseconds(millis)
                        val date = instant.toLocalDateTime(TimeZone.UTC).date
                        viewModel.onIntent(PaymentFormIntent.DateSelected(date))
                    }
                }) { Text("Siguiente") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onIntent(PaymentFormIntent.DismissDatePicker) }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- Lógica de Time Picker ---
    if (state.showTimePicker) {
        val timePickerState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { viewModel.onIntent(PaymentFormIntent.DismissTimePicker) },
            confirmButton = {
                TextButton(onClick = {
                    val time = LocalTime(timePickerState.hour, timePickerState.minute)
                    viewModel.onIntent(PaymentFormIntent.TimeSelected(time))
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onIntent(PaymentFormIntent.DismissTimePicker) }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    PaymentFormScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PaymentFormScreenContent(
    state: PaymentFormViewState,
    onIntent: (PaymentFormIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(horizontal = 26.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Encabezado
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (state.isEditMode) "Editar pago" else "Registrar nuevo pago",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Mantén un registro claro y detallado de todas las transacciones.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        // Formulario
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DynaSyncTextField(
                value = state.beneficiary,
                onValueChange = {
                    if(it.length <= 20) {
                        onIntent(PaymentFormIntent.BeneficiaryChange(it))
                    }
                },
                label = "Beneficiario",
                errorMessage = state.beneficiaryError,
                supportingText = "Nombre del receptor",
                modifier = Modifier.fillMaxWidth(),
                maxChars = 20,
                charCount = state.beneficiary.length
            )

            DynaSyncTextField(
                value = state.amount,
                onValueChange = {
                    if(it.length <= 10) {
                        onIntent(PaymentFormIntent.AmountChange(it))
                    }
                },
                label = "Monto",
                errorMessage = state.amountError,
                supportingText = "Cantidad total",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                maxChars = 10,
                charCount = state.amount.length
            )

            // Selector de Fecha y Hora (Read Only TextField)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.displayDate,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha y Hora") },
                    placeholder = { Text("Selecciona una fecha") },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_calendar_today_24),
                            contentDescription = "Calendario"
                        )
                    },
                    isError = state.paymentDateTimeError != null,
                    supportingText = {
                        if (state.paymentDateTimeError != null) {
                            Text(text = state.paymentDateTimeError!!)
                        } else {
                            Text(text = "Toque para seleccionar")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { onIntent(PaymentFormIntent.OpenDatePicker) }
                )
            }

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.paymentType.description,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Pago") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    PaymentType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.description) },
                            onClick = {
                                onIntent(PaymentFormIntent.PaymentTypeChange(type))
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onIntent(PaymentFormIntent.SubmitForm) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = JungleTeal),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if(state.isEditMode) "Actualizar Pago" else "Guardar Pago",
                style = MaterialTheme.typography.titleMedium
            )
        }


        state.error?.let {
            Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
            onIntent(PaymentFormIntent.CleanError)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if(state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        }
    }

}