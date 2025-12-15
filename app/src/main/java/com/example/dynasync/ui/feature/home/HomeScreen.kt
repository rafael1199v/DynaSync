package com.example.dynasync.ui.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dynasync.R
import com.example.dynasync.data.repository.ProjectRepository
import com.example.dynasync.navigation.MainDestination
import com.example.dynasync.ui.theme.JungleTeal
import com.example.dynasync.utils.shimmerEffect

@Composable
fun HomeScreen(
    onProjectClick: (Int) -> Unit,
    onCreateProject: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel()
) {
    val state by homeViewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onCreateProject,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        ),
                        modifier = Modifier.padding(all = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Float Action Button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                        Text(
                            text = "Proyecto",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        }
    ) { contentPadding ->

        if(state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }
        else {
            HomeScreenContent(
                onProjectClick = onProjectClick,
                state = state,
                modifier = Modifier.padding(contentPadding)
            )
        }

    }

}


@Composable
fun HomeScreenContent(
    onProjectClick: (Int) -> Unit,
    state: HomeViewState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }

        item {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(0.2f),
                        shape = RoundedCornerShape(20.dp)
                    ).padding(all = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.user.profileImageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Hola, ${state.user.name}",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_corporate_fare_24),
                                contentDescription = "Projects"
                            )

                            Spacer(modifier = Modifier.width(12.dp))


                            Text(
                                text = "${state.projectsInProcess} Proyectos en proceso",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_pending_actions_24),
                                contentDescription = "Tasks"
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                "${state.pendingTasks} Tareas pendientes",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp)
            ) {
                Text(
                    text = "Proyectos",
                    style = MaterialTheme.typography.headlineMedium
                )

                if (state.projects.isEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_projects2_1),
                            contentDescription = "No projects image",
                            modifier = Modifier.width(285.dp).height(289.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                        )

                        Text(
                            text = "Parece que no tienes proyectos en progreso.  Crea uno nuevo!",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        state.projects.forEach { project ->
                            ProjectCard(
                                project = project,
                                onClick = {
                                    onProjectClick(project.id)
                                }
                            )
                        }
                    }
                }

            }


        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    //HomeScreen(navController = NavController(LocalContext.current))
    HomeScreenContent(
        onProjectClick = {},
        state = HomeViewState(
            projects = ProjectRepository.projects
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun HomeSkeletonLoader(
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(all = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(56.dp))

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProjectCardSkeleton(
                modifier = Modifier.fillMaxWidth()
            )
            ProjectCardSkeleton(
                modifier = Modifier.fillMaxWidth()
            )
        }


    }
}