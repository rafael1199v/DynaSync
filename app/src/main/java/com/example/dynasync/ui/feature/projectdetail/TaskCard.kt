package com.example.dynasync.ui.feature.projectdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dynasync.R
import com.example.dynasync.domain.model.Personal
import com.example.dynasync.domain.model.Task
import com.example.dynasync.ui.theme.JungleTeal
import kotlinx.datetime.LocalDate

@Composable
fun TaskCard(
    task: Task,
    onToggleTask: (taskId: Int) -> Unit,
    onDeleteTask: (taskId: Int) -> Unit,
    onEditTask: (taskId: Int) -> Unit, // Agregué este parámetro que faltaba
    modifier: Modifier = Modifier
) {
    var checkboxToggle by remember { mutableStateOf(task.isCompleted) }

    Box(
        modifier = modifier.border(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(0.2f),
            shape = MaterialTheme.shapes.medium
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(), // Asegura que ocupe el ancho
            verticalAlignment = Alignment.Top // Alineamos todo arriba
        ) {
            // 1. Checkbox a la izquierda
            Checkbox(
                checked = checkboxToggle,
                onCheckedChange = {
                    checkboxToggle = it
                    onToggleTask(task.id)
                },
                modifier = Modifier.padding(end = 8.dp) // Espacio entre check y contenido
            )

            // 2. Columna Central (Contenido + Botones en la cabecera)
            Column(
                modifier = Modifier.weight(1f) // IMPORTANTE: Ocupa todo el ancho restante
            ) {
                // --- FILA DE CABECERA (Título + Botones) ---
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // A. Título
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f) // El texto toma el espacio disponible
                            .padding(end = 8.dp) // Margen para no pegarse a los botones
                    )

                    // B. Botones de Acción (Ahora son parte del flujo, no flotantes)
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        // Botón Borrar
                        IconButton(
                            onClick = { onDeleteTask(task.id) },
                            modifier = Modifier.size(32.dp) // Tamaño controlado
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_delete_24),
                                contentDescription = "Borrar",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Botón Editar
                        IconButton(
                            onClick = { onEditTask(task.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_edit_square_24),
                                contentDescription = "Editar",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // --- DETALLES DEBAJO DEL TÍTULO ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    // Responsable
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_business_center_24),
                            contentDescription = "Responsable",
                            modifier = Modifier.size(16.dp) // Iconos pequeños para detalles
                        )
                        Text(
                            text = if (task.personal == null) "Sin asignar" else "${task.personal.name} ${task.personal.lastname}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Fecha
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_calendar_today_24),
                            contentDescription = "Date",
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Fin: ${task.finishDate}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    val task = Task(
        id = 1,
        title = "012345678901234",
        isCompleted = false,
        personal = Personal(
            id = 1,
            name = "Carlos",
            lastname = "Salazar",
            charge = "Developer"
        ),
        finishDate = LocalDate(year = 2023, month = 12, day = 1),
    )
    TaskCard(
        task = task,
        onDeleteTask = {},
        onToggleTask = {},
        onEditTask = {}
    )
}
