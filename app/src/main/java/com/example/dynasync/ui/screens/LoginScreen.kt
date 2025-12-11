package com.example.dynasync.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dynasync.R
import com.example.dynasync.ui.event.LoginUiEvent
import com.example.dynasync.ui.states.LoginViewState
import com.example.dynasync.ui.theme.JungleTeal
import com.example.dynasync.viewmodels.LoginViewModel


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()


    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when(event) {
                is LoginUiEvent.NavigateToHome -> {
                    onLoginSuccess()
                }
            }
        }
    }

    LoginScreenContent(
        state = state,
        modifier = modifier,
        onUpdateEmail = { newEmail ->
            viewModel.updateEmail(newEmail)
        },
        onUpdatePassword = { newPassword ->
            viewModel.updatePassword(newPassword)
        },
        onSubmitForm = {
            println("Antes del form")
            viewModel.submitForm()
        }
    )
}


@Composable
fun LoginScreenContent(
    state: LoginViewState,
    onUpdateEmail: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
    onSubmitForm: () -> Unit,
    modifier: Modifier = Modifier
) {

    var passwordVisible by remember { mutableStateOf(value = false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterVertically),

        ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_dyna_sync),
                contentDescription = "Logo DynaSync",
                modifier = Modifier.height(143.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "La organización nunca fué tan facil",
                style = MaterialTheme.typography.bodyLarge
            )
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {
                        onUpdateEmail(it)
                    },
                    placeholder = {
                        Text(text = "Ingresa tu correo")
                    },
                    label = {
                        Text(text = "Correo")
                    },
                    isError = state.emailError != null,
                    supportingText = {
                        if(state.emailError != null) {
                            Text(
                                text = state.emailError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onUpdatePassword(it)
                    },
                    visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    placeholder = {
                        Text(text = "Ingresa tu contraseña")
                    },
                    label = {
                        Text(text = "Contraseña")
                    },
                    isError = state.passwordError != null,
                    supportingText = {
                        if(state.passwordError != null) {
                            Text(
                                text = state.passwordError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )
            }

            if(state.loginError != null) {
                Text(
                    text = state.loginError
                )
            }

            Button(
                onClick = {
                    onSubmitForm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = JungleTeal
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }

        Column (
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextButton(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    contentColor = JungleTeal,
                    containerColor = Color.Transparent
                )
            ) {
                Text(text = "¿Olvidaste tu contraseña?")
            }
            TextButton(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    contentColor = JungleTeal,
                    containerColor = Color.Transparent
                )
            ) {
                Text(text = "¿No tienes una cuenta?")
            }


        }

    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = LoginViewState(),
        onUpdateEmail = {},
        onUpdatePassword = {},
        onSubmitForm = {}
    )
}