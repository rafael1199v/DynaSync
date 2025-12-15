package com.example.dynasync.ui.feature.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dynasync.R
import com.example.dynasync.ui.components.DynaSyncTextField
import com.example.dynasync.ui.theme.DynaSyncTheme
import com.example.dynasync.utils.createImageFile
import com.example.dynasync.utils.uriToFile

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    RegisterScreenContent(
        modifier = modifier,
        onIntent = viewModel::onIntent,
        state = state
    )

}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    onIntent: (RegisterIntent) -> Unit,
    state: RegisterViewState
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val activity = context as? Activity

    var showSourceSelectionDialog by remember { mutableStateOf(false) }
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    var passwordVisible by remember { mutableStateOf(value = false) }
    var repeatPasswordVisible by remember { mutableStateOf(value = false) }

    val maxCharsEmail = 40
    val maxCharsName = 25
    val maxCharsLastname = 25
    val maxCharsAge = 2
    val maxCharsPassword = 20

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val file = context.uriToFile(uri)
            if (file != null) {
                onIntent(RegisterIntent.ChangeProfileImageUrl(file.absolutePath))
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoPath != null) {
            onIntent(RegisterIntent.ChangeProfileImageUrl(currentPhotoPath!!))
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = context.createImageFile()
            currentPhotoPath = file.absolutePath
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            tempPhotoUri = uri
            cameraLauncher.launch(uri)
        } else {
            val shouldShowRationale = activity?.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ?: false
            if (!shouldShowRationale) showSettingsDialog = true
        }
    }

    fun openCamera() {
        val permission = Manifest.permission.CAMERA
        when {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                val file = context.createImageFile()
                currentPhotoPath = file.absolutePath
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
                tempPhotoUri = uri
                cameraLauncher.launch(uri)
            }
            activity?.shouldShowRequestPermissionRationale(permission) == true -> showRationaleDialog = true
            else -> permissionLauncher.launch(permission)
        }
    }

    fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    Box(
        modifier = modifier.padding(horizontal = 26.dp)
    ) {
        if(state.isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterVertically)
            ) {

                Box(contentAlignment = Alignment.BottomEnd) {
                    val imageModifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { showSourceSelectionDialog = true }

                    if (state.profileImageUrl != null && state.profileImageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = state.profileImageUrl,
                            contentDescription = "Profile Photo",
                            modifier = imageModifier,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = imageModifier,
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_photo_24),
                                contentDescription = "Agregar foto",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    SmallFloatingActionButton(
                        onClick = { showSourceSelectionDialog = true },
                        modifier = Modifier.size(32.dp).align(Alignment.BottomEnd),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    ) {
                        Icon(
                            painter = painterResource(if (state.profileImageUrl.isNullOrEmpty()) R.drawable.baseline_add_24 else R.drawable.baseline_edit_24),
                            contentDescription = "Editar foto",
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    if(!state.profileImageUrl.isNullOrEmpty()) {
                        SmallFloatingActionButton(
                            onClick = {
                                onIntent(RegisterIntent.ChangeProfileImageUrl(""))
                            },
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 0.dp, y = (-4).dp),
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            shape = CircleShape
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Eliminar foto",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DynaSyncTextField(
                        value = state.email,
                        onValueChange = {
                            if(it.length <= maxCharsEmail)
                                onIntent(RegisterIntent.ChangeEmail(it))
                        },
                        label = "Correo",
                        errorMessage = state.emailError,
                        supportingText = "Ingresa tu correo",
                        charCount = state.email.length,
                        maxChars = maxCharsEmail,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    DynaSyncTextField(
                        value = state.name,
                        onValueChange = {
                            if(it.length <= maxCharsName)
                                onIntent(RegisterIntent.ChangeName(it))
                        },
                        label = "Nombre",
                        errorMessage = state.nameError,
                        supportingText = "Ingresa tu nombre",
                        charCount = state.name.length,
                        maxChars = maxCharsName,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    DynaSyncTextField(
                        value = state.lastname,
                        onValueChange = {
                            if(it.length <= maxCharsLastname)
                                onIntent(RegisterIntent.ChangeLastName(it))
                        },
                        label = "Apellido",
                        errorMessage = state.lastnameError,
                        supportingText = "Ingresa tu apellido",
                        charCount = state.lastname.length,
                        maxChars = maxCharsLastname,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    DynaSyncTextField(
                        value = state.age,
                        onValueChange = {
                            if(it.length <= maxCharsAge)
                                onIntent(RegisterIntent.ChangeAge(it))
                        },
                        label = "Edad",
                        errorMessage = state.ageError,
                        supportingText = "Ingresa tu edad",
                        charCount = state.age.length,
                        maxChars = maxCharsAge,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    DynaSyncTextField(
                        value = state.password,
                        onValueChange = {
                            if(it.length <= maxCharsPassword)
                                onIntent(RegisterIntent.ChangePassword(it))
                        },
                        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible}
                            ) {
                                if(passwordVisible) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                        contentDescription = "Visibility off"
                                    )
                                }
                                else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        contentDescription = "Visibility on"
                                    )
                                }
                            }
                        },
                        label = "Contraseña",
                        errorMessage = state.passwordError,
                        supportingText = "Ingresa tu contraseña",
                        charCount = state.password.length,
                        maxChars = maxCharsPassword,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1
                    )

                    DynaSyncTextField(
                        value = state.repeatPassword,
                        onValueChange = {
                            if(it.length <= maxCharsPassword)
                                onIntent(RegisterIntent.ChangeRepeatPassword(it))
                        },
                        visualTransformation = if(repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = { repeatPasswordVisible = !repeatPasswordVisible}
                            ) {
                                if(repeatPasswordVisible) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                                        contentDescription = "Visibility off"
                                    )
                                }
                                else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                                        contentDescription = "Visibility on"
                                    )
                                }
                            }
                        },
                        label = "Repetir contraseña",
                        errorMessage = state.repeatPasswordError,
                        supportingText = "Repite tu contraseña",
                        charCount = state.repeatPassword.length,
                        maxChars = maxCharsPassword,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                    )
                }

                if(state.error != null) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = { onIntent(RegisterIntent.SubmitForm) },
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
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                TextButton(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(text = "¿Ya tienes una cuenta? Inicia sesión")
                }
            }
        }
    }

    if (showSourceSelectionDialog) {
        AlertDialog(
            onDismissRequest = { showSourceSelectionDialog = false },
            title = { Text("Foto de perfil") },
            text = { Text("Selecciona una imagen para tu perfil.") },
            confirmButton = {
                TextButton(onClick = {
                    showSourceSelectionDialog = false
                    openCamera()
                }) { Text("Cámara") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSourceSelectionDialog = false
                    openGallery()
                }) { Text("Galería") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (showRationaleDialog) {

        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Permiso necesario") },
            text = { Text("DynaSync necesita acceso a la cámara para tomar la foto.") },
            confirmButton = {
                TextButton(onClick = { showRationaleDialog = false; permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) { Text("Cancelar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }

    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Permiso bloqueado") },
            text = { Text("El permiso de cámara fue denegado permanentemente. Ve a configuración para activarlo.") },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) { Text("Ir a Configuración") }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) { Text("Cancelar") }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )


    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    DynaSyncTheme {
        RegisterScreenContent(
            onIntent = {},
            state = RegisterViewState(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
