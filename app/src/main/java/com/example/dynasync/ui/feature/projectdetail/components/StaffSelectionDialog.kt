package com.example.dynasync.ui.feature.projectdetail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.dynasync.R
import com.example.dynasync.domain.model.Personal

@Composable
fun StaffSelectionDialog(
    staffList: List<Personal>,
    onDismissRequest: () -> Unit,
    onPersonalSelected: (Personal?) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = remember(searchQuery, staffList) {
        if (searchQuery.isBlank()) staffList
        else staffList.filter {
            "${it.name} ${it.lastname}".contains(searchQuery, ignoreCase = true)
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp) // Altura máxima controlada
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Seleccionar Responsable",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Buscador dentro del diálogo
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar nombre...") },
                    leadingIcon = { Icon(painterResource(R.drawable.baseline_search_24), "Buscar") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { onPersonalSelected(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sin responsable / Desasignar", color = MaterialTheme.colorScheme.error)
                }

                HorizontalDivider()

                LazyColumn {
                    items(filteredList) { person ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text("${person.name} ${person.lastname}", fontWeight = FontWeight.Bold)
                                    Text(person.charge, style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = { onPersonalSelected(person) }
                        )
                    }
                }
            }
        }
    }
}