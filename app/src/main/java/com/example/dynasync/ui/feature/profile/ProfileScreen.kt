package com.example.dynasync.ui.feature.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.dynasync.R
import com.example.dynasync.domain.model.User
import com.example.dynasync.ui.feature.createproject.CreateProjectUiEvent
import com.example.dynasync.ui.theme.AppThemeMode

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val themeMode by viewModel.currentTheme.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is ProfileUiEvent.NavigateToLogin -> {
                    onLogout()
                }
            }
        }
    }

    Box(
        modifier = modifier.padding()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        else if(state.error != null) {
            Text(
                text = state.error!!,
                modifier = Modifier.align(Alignment.Center).padding(26.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )
        }
        else {
            state.user?.let { user ->
                ProfileScreenContent(
                    state = state,
                    onIntent = viewModel::onIntent,
                    modifier = modifier,
                    themeMode = themeMode
                )
            }
        }
    }

}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    onIntent: (ProfileIntent) -> Unit,
    state: ProfileViewState,
    themeMode: AppThemeMode = AppThemeMode.SYSTEM
) {

    val scrollState = rememberScrollState()

    var showLogoutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.user?.profileImageUrl)
                    .crossfade(true)
                    .build(),

                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${state.user?.name} ${state.user?.lastName}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Edad: ${state.user?.age} años",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            ProfileInfoRow(icon = painterResource(id = R.drawable.person_24), label = "Nombre", value = state.user?.name ?: "Nombre")
            ProfileInfoRow(icon = painterResource(id = R.drawable.person_24), label = "Apellido", value = state.user?.lastName ?: "Apellido")
            ProfileInfoRow(icon = painterResource(id = R.drawable.baseline_email_24), label = "Correo", value = state.userEmail ?: "Correo")
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(all = 26.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Temas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            ProfileRowTheme(
                onSelect = { onIntent(ProfileIntent.ChangeTheme(AppThemeMode.DARK))},
                isSelected = AppThemeMode.DARK == themeMode,
                icon = painterResource(id = R.drawable.baseline_dark_mode_24),
                title = "Oscuro"
            )

            ProfileRowTheme(
                onSelect = { onIntent(ProfileIntent.ChangeTheme(AppThemeMode.LIGHT))},
                isSelected = AppThemeMode.LIGHT == themeMode,
                icon = painterResource(id = R.drawable.baseline_light_mode_24),
                title = "Claro"
            )

            ProfileRowTheme(
                onSelect = { onIntent(ProfileIntent.ChangeTheme(AppThemeMode.SYSTEM))},
                isSelected = AppThemeMode.SYSTEM == themeMode,
                icon = painterResource(id = R.drawable.outline_routine_24),
                title = "Sistema"
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                showLogoutDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_logout_24),
                contentDescription = "Cerrar Sesión"
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Cerrar Sesión",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }


    if(showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que quieres salir de tu cuenta?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onIntent(ProfileIntent.Logout)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun ProfileInfoRow(
    icon: Painter,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = icon,
            contentDescription = "Profile Icon",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileRowTheme(
    onSelect: () -> Unit,
    isSelected: Boolean,
    icon: Painter,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        RadioButton(
            selected = isSelected,
            onClick = onSelect,
        )

        Spacer(modifier = Modifier.width(4.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = icon,
            contentDescription = "Profile Icon Theme",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(28.dp)
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        modifier = Modifier.fillMaxSize(),
        onIntent = {},
        state = ProfileViewState(
            user = User(
                id = "1",
                name = "Daniel Roland",
                lastName = "Penaranda Colque",
                age = 22,
                profileImageUrl = "",
                projects = emptyList()
            )
        )
    )
}


