package com.example.dynasync.ui.feature.projectdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dynasync.R
import com.example.dynasync.domain.Personal
import com.example.dynasync.domain.Project
import com.example.dynasync.domain.Task
import kotlinx.datetime.LocalDate

@Composable
fun ProjectDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectDetailViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()


    ProjectDetailScreenContent(
        state = state,
        modifier = modifier
    )

}

@Composable
fun ProjectDetailScreenContent(
    state: ProjectDetailState,
    modifier: Modifier = Modifier
) {

    //Text(text = "${state.project.toString()}")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(
            space = 20.dp
        ),
        modifier = modifier
    ) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.project?.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Project Image",
                modifier = Modifier.fillMaxWidth().height(280.dp),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
                verticalArrangement = Arrangement.spacedBy(
                    space = 20.dp
                ),
            ) {
                Text(
                    text = state.project?.title ?: "Proyecto",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.W600
                )

                Text(
                    text = state.project?.description ?: "Description",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Objetivo",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = state.project?.objective ?: "Contenido del objetivo",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Finalización: ${state.project?.finishDate.toString()}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                //Seccion de tareas

                Text(
                    text = "Tareas",
                    style = MaterialTheme.typography.titleLarge
                )
            }

        }

        if(state.project?.tasks?.isEmpty() == true) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_task2_1),
                        contentDescription = "No tasks image",
                        modifier = Modifier
                            .width(260.dp)
                            .height(271.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )

                    Text(
                        text = "Parece que no tienes tareas nuevas. Añade una!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
        else {
            items(state.project?.tasks ?: emptyList()) { task ->
                TaskCard(task = task, modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp))
            }
        }


        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
                verticalArrangement = Arrangement.spacedBy(
                    space = 4.dp
                ),
            ) {
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                           painter = painterResource(id = R.drawable.baseline_edit_24),
                            contentDescription = "Edit"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Editar proyecto")
                    }

                }

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete_filled),
                            contentDescription = "Delete"
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(text = "Eliminar proyecto")
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProjectDetailScreenPreview() {
    val state = ProjectDetailState(
        project = Project(
            id = 1,
            title = "Project Management",
            description = "La gestión de un proyecto es importante para el crecimiento empresarial y establecer las directrices de las personas que tiene la responsabilidad de manejar y gestionar a una gran cantidad de personas.",
            objective = "Proporcionar una aplicación fiable y fácil de usuario para cualquier emprendedor ",
            finishDate = LocalDate(year = 2023, month = 12, day = 1),
            imageUrl = "https://images.unsplash.com/photo-1761839256791-6a93f89fb8b0?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            tasks = listOf(
                Task(
                    id = 1,
                    title = "Task 1",
                    description = "Description of task 1",
                    isCompleted = true,
                    personal = Personal(
                        id = 1,
                        name = "Carlos",
                        lastname = "Salazar",
                        charge = "Developer"
                    ),
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                ),
                Task(
                    id = 2,
                    title = "Task 2",
                    description = "Description of task 2",
                    isCompleted = false,
                    personal = Personal(
                        id = 1,
                        name = "Rafael",
                        lastname = "Vargas",
                        charge = "Developer"
                    ),
                    finishDate = LocalDate(year = 2023, month = 12, day = 1),
                )
            )
            //tasks = emptyList()
        )
    )

    ProjectDetailScreenContent(
        state = state,
        modifier = Modifier.fillMaxSize()
    )
}
