package com.example.dynasync.ui.feature.projectdetail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    modifier: Modifier = Modifier
) {

    var checkboxToggle by remember { mutableStateOf(task.isCompleted) }

    Box(
        modifier = modifier.border(
            width = 1.dp,
            color = Color.Black.copy(0.2f),
            shape = MaterialTheme.shapes.medium
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).padding(end = 40.dp)
        ) {
            Checkbox(
                checked = checkboxToggle,
                onCheckedChange = {
                    checkboxToggle = it
                    onToggleTask(task.id)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = JungleTeal
                )
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_business_center_24),
                            contentDescription = "Responsable"
                        )

                        Text(
                            text = "Responsable: ${task.personal.name} ${task.personal.lastname}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_calendar_today_24),
                            contentDescription = "Date"
                        )

                        Text(
                            text = "Finalizacion: ${task.finishDate.toString()}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            }
        }

        IconButton(
            onClick = {
                onDeleteTask(task.id)
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_delete_24),
                contentDescription = "Editar tarea",
                modifier = Modifier.width(24.dp).height(24.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    val task = Task(
        id = 1,
        title = "Task 1",
        description = "Description of task 1",
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
        onToggleTask = {}
    )
}
