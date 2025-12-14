package com.example.dynasync.ui.feature.staff.form

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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dynasync.R
import com.example.dynasync.ui.components.DynaSyncTextField
import com.example.dynasync.ui.theme.JungleTeal
import com.example.dynasync.utils.createImageFile
import com.example.dynasync.utils.uriToFile
import java.io.File



@Composable
fun StaffFormScreen(
    onSubmitFormSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StaffFormViewModel = viewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is StaffFormUiEvent.NavigateToStaff -> {
                    onSubmitFormSuccess()
                }

            }
        }
    }



    StaffFormScreenContent(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )


}


@Composable
fun StaffFormScreenContent(
    state: StaffFormViewState,
    onIntent: (StaffFormIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSourceSelectionDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity

    val scrollState = rememberScrollState()

    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoPath by remember { mutableStateOf<String?>(null) }

    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val nameMaxChars = 20
    val lastNameMaxChars = 20
    val chargeMaxChars = 20

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val file = context.uriToFile(uri)
            if (file != null) {
                onIntent(StaffFormIntent.ImageUrlChange(file.absolutePath))
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentPhotoPath != null) {
            onIntent(StaffFormIntent.ImageUrlChange(currentPhotoPath!!))
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

    Column(
        modifier = modifier.
        padding(start = 26.dp, end = 26.dp)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if(state.isEditMode) {
                Text(
                    text = "Actualiza tu personal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Una empresa exitosa siempre tiene en cuenta a sus empleados y los apoya en todo momento.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            else {
                Text(
                    text = "Agrega a un nuevo personal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Cada gran proyecto empezó con un solo integrante. La dedicacion y perseverancia es la clave para el exito",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }

        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(contentAlignment = Alignment.BottomEnd) {
                if (state.imageUrl != null) {
                    AsyncImage(
                        model = if(state.isEditMode) state.imageUrl else File(state.imageUrl),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SmallFloatingActionButton(
                            onClick = {
                                onIntent(StaffFormIntent.ImageUrlChange(null))
                            },
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = Color.White
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_filled),
                                contentDescription = "Eliminar imagen"
                            )
                        }


                        SmallFloatingActionButton(
                            onClick = { showSourceSelectionDialog = true },
                            modifier = Modifier.padding(8.dp),
                            containerColor = JungleTeal
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_edit_square_24),
                                contentDescription = "Cambiar", tint = Color.White
                            )
                        }
                    }


                } else {

                    Button(
                        onClick = { showSourceSelectionDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = JungleTeal
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_photo_24),
                            contentDescription = "Upload image"
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Subir imagen")
                    }

                }
            }



            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DynaSyncTextField(
                    value = state.name,
                    onValueChange = {
                        if(it.length <= nameMaxChars) {
                            onIntent(StaffFormIntent.NameChange(it))
                        }
                    },
                    label = "Nombre",
                    errorMessage = state.nameError,
                    supportingText = "Nombre del personal",
                    charCount = state.name.length,
                    maxChars = nameMaxChars,
                    modifier = Modifier.fillMaxWidth(),
                )

                DynaSyncTextField(
                    value = state.lastname,
                    onValueChange = {
                        if(it.length <= lastNameMaxChars) {
                            onIntent(StaffFormIntent.LastNameChange(it))
                        }
                    },
                    label = "Apellido",
                    errorMessage = state.lastnameError,
                    supportingText = "Apellido del personal",
                    charCount = state.lastname.length,
                    maxChars = lastNameMaxChars,
                    modifier = Modifier.fillMaxWidth(),
                )


                DynaSyncTextField(
                    value = state.charge,
                    onValueChange = {
                        if(it.length <= chargeMaxChars)  {
                            onIntent(StaffFormIntent.ChargeChange(it))
                        }
                    },
                    label = "Cargo",
                    errorMessage = state.chargeError,
                    supportingText = "Cargo del personal",
                    charCount = state.charge.length,
                    maxChars = chargeMaxChars,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Button(
            onClick = {
                onIntent(StaffFormIntent.SubmitForm)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = JungleTeal
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if(state.isEditMode) "Editar personal" else "Crear personal",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    if (showSourceSelectionDialog) {

        AlertDialog(
            onDismissRequest = { showSourceSelectionDialog = false },
            title = { Text("Seleccionar imagen") },
            text = { Text("¿De dónde quieres obtener la imagen para tu proyecto?") },
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
            }
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
            }
        )
    }

    if (showSettingsDialog) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                primary = JungleTeal,
                onPrimary = Color.White,
                surface = Color.White,
                onSurface = Color.Black
            )
        ) {
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
                }
            )
        }

    }

    if(state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.5f))
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
        }
    }
}