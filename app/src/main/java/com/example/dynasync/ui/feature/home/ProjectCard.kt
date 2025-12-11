package com.example.dynasync.ui.feature.home

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dynasync.R
import com.example.dynasync.domain.model.Project
import com.example.dynasync.ui.theme.WisteriaBlue
import kotlinx.datetime.LocalDate

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Card(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
            shape = RoundedCornerShape(size = 12.dp)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Companion.Transparent
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.Companion.height(IntrinsicSize.Min)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(project.imageUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Image",
                contentScale = ContentScale.Companion.Crop,
                modifier = Modifier.Companion.width(108.dp).fillMaxHeight()
            )

            Column(
                modifier = Modifier.Companion.padding(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.Companion.width(4.dp))

                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Companion.Ellipsis,
                        maxLines = 2
                    )
                }

                Spacer(modifier = Modifier.Companion.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Progreso:",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    LinearProgressIndicator(
                        progress = {
                            0.5f
                        },
                        color = WisteriaBlue,
                        modifier = Modifier.Companion.height(8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    val project = Project(
        id = 1,
        title = "DynaSync",
        objective = "Mejorar la productividad de las personas",
        description = "Esta tiene que ser una descripcion muy larga para que las personas puedan ver hasta donde se puede extender..",
        finishDate = LocalDate(year = 2023, month = 12, day = 1),
        imageUrl = "https://images.unsplash.com/photo-1761839257864-c6ccab7238de?q=80&w=1171&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        tasks = emptyList()
    )

    ProjectCard(project = project, onClick = {})
}