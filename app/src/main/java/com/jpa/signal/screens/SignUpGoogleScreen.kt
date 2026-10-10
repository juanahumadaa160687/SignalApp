package com.jpa.signal.screens

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.data.Usuario
import com.jpa.signal.data.UsuarioRepo
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.surface_light
import com.jpa.signal.ui.theme.tertiary_light
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpGoogle (navHostController: NavHostController, auth: FirebaseAuth) {

    val scope = rememberCoroutineScope()

    val rut = rememberTextFieldState("")
    val nombre = rememberTextFieldState("")
    val apellido = rememberTextFieldState("")
    val edad = rememberTextFieldState("")
    val telefono = rememberTextFieldState("")
    val direccion = rememberTextFieldState("")

    //Opciones de género
    val genderOptions = listOf("Masculino", "Femenino", "Otro")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(genderOptions[0])}

    //Variable para el texto del AlertDialog
    var text by remember { mutableStateOf("") }

    //Variable que controla la visibilidad del AlertDialog
    var openAlertDialog = remember { mutableStateOf(false) }

    //Si la variable openAlertDialog es true, se muestra el AlertDialog
    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                title = {
                    when {
                        text == "Debe completar todos los campos" ->
                            Text(
                                text = "Oops... algo anda mal",
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 22.sp,
                                lineHeight = 28.sp,
                                letterSpacing = 0.sp,
                            )
                        else -> Text(
                            text = "Registro Exitoso",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp,
                        )
                    }
                },
                text = {
                    when {
                        text == "Debe completar todos los campos" ->
                            Text(
                                text = text,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.5.sp,
                                textAlign = TextAlign.Center,
                            )
                        else -> Text(
                            text = text,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                },
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

        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Registro de Usuario",
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

        Column(modifier = Modifier
            .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = rut,
                        placeholder = { Text("RUT") },

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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { rut.clearText() },
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
                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = nombre,
                        placeholder = { Text("Nombre") },

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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { nombre.clearText() },
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
                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = apellido,
                        placeholder = { Text("Apellido") },

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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { apellido.clearText() },
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
                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = edad,
                        placeholder = { Text("Edad") },

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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { edad.clearText() },
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
                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = telefono,
                        placeholder = { Text("Número de teléfono") },

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
                                painter = painterResource(id = R.drawable.ic_phone),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { telefono.clearText() },
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
                    Spacer(modifier = Modifier.height(40.dp))

                    TextField(
                        state = direccion,
                        placeholder = { Text("Dirección") },

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
                                painter = painterResource(id = R.drawable.ic_address),
                                contentDescription = null,
                                tint = primary_light,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { direccion.clearText() },
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
                    Spacer(modifier = Modifier.height(40.dp))

                    Column(
                        modifier = Modifier.selectableGroup(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Género",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primary_light,
                            modifier = Modifier.padding(bottom = 8.dp, start = 16.dp)
                        )
                        genderOptions.forEach { text ->
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = { onOptionSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = null,
                                    colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                        selectedColor = primary_light,
                                        unselectedColor = primary_light
                                    )
                                )
                                Text(
                                    text = text,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    letterSpacing = 0.25.sp,
                                    color = primary_light,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }


                item {
                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            if (rut.text.isEmpty() || nombre.text.isEmpty() || apellido.text.isEmpty() || edad.text.isEmpty() || telefono.text.isEmpty() || direccion.text.isEmpty() ){
                                openAlertDialog.value = true
                                text = "Por favor, complete todos los campos"
                            }
                            else{
                                scope.launch {
                                    UsuarioRepo.addUsuario(
                                        Usuario(
                                            uid = auth.currentUser!!.uid,
                                            rut = rut.text as String,
                                            nombre = nombre.text as String,
                                            apellido = apellido.text as String,
                                            edad = edad.text as String,
                                            genero = selectedOption,
                                            telefono = telefono.text as String,
                                            correo = auth.currentUser!!.email.toString(),
                                            direccion = direccion.text as String,
                                        )
                                    )
                                }
                                openAlertDialog.value = true
                                text = "Usuario creado exitosamente"
                                navHostController.navigate("home")
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
                item {
                    TextButton(
                        onClick = { navHostController.navigate("sign-in") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ya tengo una cuenta",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = tertiary_light
                        )
                    }
                }
            }
        }
    }

}