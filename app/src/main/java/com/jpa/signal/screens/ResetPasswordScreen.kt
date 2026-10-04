package com.jpa.signal.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.*
import com.jpa.signal.R

@Composable
fun ResetPasswordScreen(navHostController: NavHostController, auth: FirebaseAuth) {

    val password = rememberTextFieldState("")
    val confirmPassword = rememberTextFieldState("")
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

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
                    text == "Contraseña actualizada correctamente." -> R.drawable.ic_success
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
        SecureTextField(
            state = password,
            placeholder = { Text("Contraseña") },
            modifier =
                when {
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
                    painter = painterResource(id = R.drawable.ic_pass),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { showPassword = !showPassword },
                ) {
                    Icon(
                        painter = painterResource(id = if (showPassword) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = "Mostrar - ocultar contraseña",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            textObfuscationMode = if (showPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        )

        Spacer(modifier = Modifier.requiredSize(24.dp))

        SecureTextField(
            state = confirmPassword,
            placeholder = { Text("Confirmar Contraseña") },
            modifier =
                when {
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
                    painter = painterResource(id = R.drawable.ic_pass),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { showConfirmPassword = !showConfirmPassword},
                ) {
                    Icon(
                        painter = painterResource(id = if (showConfirmPassword) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = "Mostrar - ocultar contraseña",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            textObfuscationMode = if (showConfirmPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        )

        Spacer(modifier = Modifier.requiredSize(40.dp))

        Button(
            onClick = {
                if (password.text.isEmpty() || confirmPassword.text.isEmpty()){
                    auth.currentUser?.updatePassword(password.text.toString())
                    text = "Todos los campos son obligatorios."
                    openAlertDialog.value = true
                }else if (password.text != confirmPassword.text){
                    text = "Las contraseñas no coinciden."
                    openAlertDialog.value = true
                }else if (password.text.length !in 8..22 || password.text.contains(Regex("[^A-Za-z0-9]"))){
                    text = "La contraseña debe tener entre 8 y 22 caracteres, al menos un número una letra mayúscula y  una minúscula "
                    openAlertDialog.value = true
                }else{
                    auth.currentUser?.updatePassword(password.text.toString())
                    text = "Contraseña actualizada correctamente."
                    openAlertDialog.value = true
                }
            }
        ){
            Text(
                text = "Enviar",
            )
        }
    }
}