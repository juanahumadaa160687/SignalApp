package com.jpa.signal.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.tertiary_light

/*Pantalla de Recuperación de Contraseña
*
*  @input: navHostController: Controlador de navegación de Jetpack Compose y auth: Instancia de FirebaseAuth para autenticación.
*  @output: Pantalla de recuperación de contraseña con campo de texto para el correo electrónico y botón para enviar el enlace de restablecimiento de contraseña.
*
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navHostController: NavHostController, auth: FirebaseAuth) {

    //Variable para el correo electrónico ingresado por el usuario
    var emailState = rememberTextFieldState("")

    //Variable que controla la visibilidad del AlertDialog
    val openAlertDialog = remember { mutableStateOf(false) }

    //Variable que almacena el texto del AlertDialog
    var text by remember { mutableStateOf("") }

    //Si la variable openAlertDialog es true, se muestra el AlertDialog
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Recuperar Contraseña",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp,
                    color = background_light) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primary_light
                ),
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Volver",
                            tint = background_light,
                            modifier = Modifier.requiredSize(24.dp)
                        )
                    }
                }
            )
        }

    ) {innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
           Column(
               modifier = Modifier.padding(16.dp).fillMaxSize(),
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
                           color = primary_light,
                           shape = RoundedCornerShape(25.dp)
                       ),
                   shape = RoundedCornerShape(25.dp),
                   colors = TextFieldDefaults.colors(
                       focusedContainerColor = Color.Transparent,
                       unfocusedContainerColor = Color.Transparent,
                       focusedIndicatorColor = Color.Transparent,
                       unfocusedIndicatorColor = Color.Transparent,
                       focusedTextColor = tertiary_light,
                       unfocusedTextColor = primary_light,
                       focusedPlaceholderColor = Color.Gray,
                       unfocusedPlaceholderColor = primary_light
                   ),
                   leadingIcon = {
                       Icon(
                           painter = painterResource(id = R.drawable.ic_email),
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
                       if (emailState.text.isNotEmpty() && emailState.text.contains("@")) {
                           //Si el correo electrónico coincide con el de la cuenta, se envía el enlace de restablecimiento de contraseña
                           auth.sendPasswordResetEmail(emailState.text.toString())
                           text = "Revisa tu correo electrónico para restablecer tu contraseña."
                           openAlertDialog.value = true
                           navHostController.navigate("reset-password")
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
                   },
                   modifier =
                       when {
                           currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                           else -> Modifier.fillMaxWidth(0.5f)
                       }
                           .height(56.dp),
                   colors = ButtonDefaults.buttonColors(
                       containerColor = primary_light,
                   )
               ) {
                   Text("Enviar")
               }
           }
        }
    }
}