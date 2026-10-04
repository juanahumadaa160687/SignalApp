package com.jpa.signal.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R

@Composable
fun ForgotPasswordScreen(navHostController: NavHostController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    val email = currentUser?.email

    var emailState = rememberTextFieldState("")

    val openAlertDialog = remember { mutableStateOf(false) }

    var text = remember { mutableStateOf("").toString() }

    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                title = { Text("Cambiar Contraseña", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.tertiary) },
                text = { Text(text, color = MaterialTheme.colorScheme.primary) },
                confirmButton = {
                    TextButton(onClick = { openAlertDialog.value = false }) {
                        Text("Aceptar")
                    }
                },
                icon = { Icon(painter = painterResource(id = when {
                    text == "Revisa tu correo electrónico para restablecer tu contraseña." -> R.drawable.ic_success
                    else -> R.drawable.ic_error
                }), contentDescription = null, modifier = Modifier.requiredSize(32.dp)) },
                containerColor = MaterialTheme.colorScheme.surface,
                textContentColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.tertiary
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            state = emailState,
            placeholder = { Text("Correo electrónico") },

            modifier =
                when{
                    currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                    else -> Modifier.fillMaxWidth(0.5f)
                }.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.large
                ),
            shape = MaterialTheme.shapes.large,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.tertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_phone),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { emailState.clearText() },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clear),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
        )

        Spacer(modifier = Modifier.size(20.dp))

        Button(
            onClick = {
                if (emailState.text.isNotEmpty() && emailState.text.contains("@") && emailState.text == email) {
                    auth.sendPasswordResetEmail(emailState.text.toString())
                    text = "Revisa tu correo electrónico para restablecer tu contraseña."
                    openAlertDialog.value = true
                } else if (emailState.text.isEmpty()) {
                    text = "El correo electrónico no puede estar vacío."
                    openAlertDialog.value = true

                }else if (!emailState.text.contains("@")) {
                    text = "El correo electrónico no es válido."
                    openAlertDialog.value = true
                }else{
                    text = "El correo electrónico no coincide con el de la cuenta."
                    openAlertDialog.value = true
                }
            }
        ) {
            Text("Reset Password")
        }
    }
}