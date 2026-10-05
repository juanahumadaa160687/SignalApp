package com.jpa.signal.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecureTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.ui.theme.app_gradient
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.surface_light
import com.jpa.signal.ui.theme.tertiary_light

@OptIn(ExperimentalMaterial3Api::class)

/*Pantalla de inicio de sesión
*
*  @input: navHostController: Controlador de navegación de Jetpack Compose y auth: Instancia de FirebaseAuth para autenticación.
*  @output: Pantalla de inicio de sesión con campos de texto para el correo electrónico y la contraseña, botones para iniciar sesión y registrarse.
*
*/
@Composable
fun SignInScreen(navHostController: NavHostController, auth: FirebaseAuth){

    /*
    Variables para el inicio de sesión
    email: Correo electrónico del usuario
    password: Contraseña del usuario
    showPassword: Variable para mostrar o ocultar la contraseña
    */

    var email = remember { TextFieldState("") }
    var password  = remember { TextFieldState("") }

    var showPassword by remember { mutableStateOf(false) }

    var text by remember { mutableStateOf("") }

    //Variable que controla la visibilidad del AlertDialog
    val openAlertDialog = remember { mutableStateOf(false) }

    //Si la variable openAlertDialog es true, se muestra el AlertDialog
    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                title = { Text("Error",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    letterSpacing = 0.sp,
                    color = tertiary_light) },
                text = { Text(
                    text = text,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center,
                    color = primary_light) },
                confirmButton = {
                    TextButton(onClick = { openAlertDialog.value = false }) {
                        Text("Aceptar")
                    }
                },
                icon = { Icon(painter = painterResource(id = R.drawable.ic_error), contentDescription = null, modifier = Modifier.requiredSize(32.dp)) },
                containerColor = surface_light,
                textContentColor = primary_light,
                titleContentColor = tertiary_light,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = background_light,

        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Inicio de Sesión",
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

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                item {

                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = email,
                        placeholder = { Text("Correo electrónico") },
                        // Modificador que permite ajustar el tamaño del TextField de acuerdo a la orientación de la pantalla
                        modifier =
                            when{
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            }.border(
                            width = 1.dp,
                            color = primary_light,
                            shape = MaterialTheme.shapes.large
                        ),
                        shape = MaterialTheme.shapes.large,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = background_light,
                            unfocusedContainerColor = background_light,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = primary_light,
                            unfocusedTextColor = tertiary_light,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = primary_light
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { email.clearText() },
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clear),
                                    contentDescription = null,
                                    tint = primary_light,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                    )
                }

                item {

                    Spacer(modifier = Modifier.height(24.dp))


                    SecureTextField(
                        state = password,
                        placeholder = { Text("Contraseña") },
                        // Modificador que permite ajustar el tamaño del TextField de acuerdo a la orientación de la pantalla
                        modifier =
                            when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            }.border(
                                width = 1.dp,
                                color = primary_light,
                                shape = MaterialTheme.shapes.large
                        ),
                        shape = MaterialTheme.shapes.large,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = background_light,
                            unfocusedContainerColor = background_light,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = primary_light,
                            unfocusedTextColor = tertiary_light,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = primary_light
                        ),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_pass),
                                contentDescription = null,
                                tint = primary_light,
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
                                    tint = primary_light,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        textObfuscationMode = if (showPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
                    )
                }
                item{
                    Row(
                        // Modificador que permite ajustar el tamaño del Link de acuerdo a la orientación de la pantalla
                        modifier =
                            when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            },
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { navHostController.navigate("forgot-password") },
                        ) {
                            Text("¿Olvidaste tu contraseña?",
                                color = primary_light)
                        }
                    }
                }

                item {

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        // Función que permite iniciar sesión con el correo electrónico y la contraseña ingresados
                        onClick = {
                            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                                auth.signInWithEmailAndPassword(
                                    email.text.toString(),
                                    password.text.toString()
                                )
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            navHostController.navigate("home")
                                        } else {
                                            text = "El correo electrónico o la contraseña son incorrectos."
                                            openAlertDialog.value = true
                                        }
                                    }
                                } else {
                                    text = "Por favor, ingrese un correo electrónico y una contraseña."
                                    openAlertDialog.value = true
                            }
                        },
                        // Modificador que permite ajustar el tamaño del Botón de acuerdo a la orientación de la pantalla
                        modifier =
                            when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            }
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primary_light
                        )
                    ) {
                        Text("Iniciar Sesión", color = background_light)
                    }
                }

                item{

                    Spacer(modifier = Modifier.height(36.dp))


                    TextButton(
                        onClick = { navHostController.navigate("sign-up") },
                        // Modificador que permite ajustar el tamaño del TextButton de acuerdo a la orientación de la pantalla
                        modifier =
                            when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            }
                    ) {
                        Text("¿No tienes una cuenta? Regístrate", color = primary_light)
                    }
                }
            }
        }
    }
}