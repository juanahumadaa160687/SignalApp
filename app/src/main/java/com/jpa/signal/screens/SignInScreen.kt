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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.ui.theme.app_gradient
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.tertiary_light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navHostController: NavHostController, auth: FirebaseAuth){

    var email = remember { TextFieldState("") }
    var password  = remember { TextFieldState("") }

    var showPassword by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = background_light,

        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Inicio de Sesión",
                    style = MaterialTheme.typography.titleLarge,
                    color = background_light) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = tertiary_light
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
                        onClick = {
                            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    navHostController.navigate("home")
                                } else {
                                    println("Error: ${task.exception}")
                                    println("Error: ${email}")

                                    email.clearText()
                                    password.clearText()
                                }
                            }

                        },
                        modifier =
                            when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            }
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tertiary_light
                        )
                    ) {
                        Text("Iniciar Sesión", color = background_light)
                    }
                }

                item{

                    Spacer(modifier = Modifier.height(36.dp))

                    TextButton(
                        onClick = { navHostController.navigate("sign-up") },
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