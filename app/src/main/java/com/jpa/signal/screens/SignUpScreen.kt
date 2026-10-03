package com.jpa.signal.screens

import android.app.AlertDialog
import android.os.Message
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.data.Usuario
import com.jpa.signal.data.UsuarioRepo
import com.jpa.signal.ui.theme.app_gradient
import kotlinx.coroutines.launch
import kotlin.math.sign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navHostController: NavHostController, auth: FirebaseAuth){

    val scope = rememberCoroutineScope()

    val rut = rememberTextFieldState("")
    val nombre = rememberTextFieldState("")
    val apellido = rememberTextFieldState("")
    val edad = rememberTextFieldState("")
    val telefono = rememberTextFieldState("")
    val direccion = rememberTextFieldState("")
    val email = rememberTextFieldState("")
    val password = rememberTextFieldState("")
    val confirmPassword = rememberTextFieldState("")

    val genderOptions = listOf("Masculino", "Femenino", "Otro")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(genderOptions[0])}

    var text: String = remember { mutableStateOf("").toString() }

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val openAlertDialog = remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                title = { Text("Error", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.tertiary) },
                text = { Text(text, color = MaterialTheme.colorScheme.primary) },
                confirmButton = {
                    TextButton(onClick = { openAlertDialog.value = false }) {
                        Text("Aceptar")
                    }
                },
                icon = { Icon(painter = painterResource(id = R.drawable.ic_error), contentDescription = null, modifier = Modifier.requiredSize(32.dp)) },
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
                    text = "Registro de Usuario",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.background) },
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        colors = app_gradient,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.background,
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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                    tint = MaterialTheme.colorScheme.primary,
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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                    tint = MaterialTheme.colorScheme.primary,
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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                    tint = MaterialTheme.colorScheme.primary,
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
                                painter = painterResource(id = R.drawable.ic_person),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                    tint = MaterialTheme.colorScheme.primary,
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
                                onClick = { telefono.clearText() },
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
                                painter = painterResource(id = R.drawable.ic_address),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                    tint = MaterialTheme.colorScheme.primary,
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
                                        selectedColor = MaterialTheme.colorScheme.primary,
                                        unselectedColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyMedium.merge(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }

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
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
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
                                    tint = MaterialTheme.colorScheme.primary,
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
                }
                item{
                    Spacer(modifier = Modifier.height(24.dp))


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
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            if (email.text.isEmpty() || password.text.isEmpty() || confirmPassword.text.isEmpty() ){
                                AlertDialog.Builder(
                                    navHostController.context)
                                    .setTitle("Error")
                                    .setMessage("Todos los campos son obligatorios")
                                    .setPositiveButton("Aceptar")
                                    {
                                            dialog, _ -> dialog.dismiss()
                                    }.show()
                            }
                            else if (!email.text.contains("@")){
                                AlertDialog.Builder(
                                    navHostController.context)
                                    .setTitle("Error")
                                    .setMessage("El correo electrónico no es válido")
                                    .setPositiveButton("Aceptar")
                                    {
                                            dialog, _ -> dialog.dismiss()
                                    }.show()
                            }
                            else if(password.text != confirmPassword.text){
                                AlertDialog.Builder(
                                    navHostController.context)
                                    .setTitle("Error")
                                    .setMessage("Las contraseñas no coinciden")
                                    .setPositiveButton("Aceptar")
                                    {
                                            dialog, _ -> dialog.dismiss()
                                    }.show()
                            }
                            else if (password.text.length !in 8..22 || password.text.contains(Regex("[^A-Za-z0-9]"))){
                                AlertDialog.Builder(
                                    navHostController.context)
                                    .setTitle("Error")
                                    .setMessage("La contraseña debe tener entre 8 y 22 caracteres, al menos un número una letra mayúscula y  una minúscula ")
                                    .setPositiveButton("Aceptar")
                                    {
                                        dialog, _ -> dialog.dismiss()
                                    }.show()
                            }
                            else{
                                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener { task ->
                                    if (task.isSuccessful){
                                        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener { signInTask ->
                                            if (signInTask.isSuccessful) {
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
                                                            correo = email.text as String,
                                                            direccion = direccion.text as String
                                                        )
                                                    )
                                                }
                                                navHostController.navigate("user-profile")

                                            } else {
                                                text = signInTask.exception.toString()
                                                openAlertDialog.value = true
                                            }
                                        }
                                    }
                                    else {
                                        text = task.exception.toString()
                                        openAlertDialog.value = true
                                    }
                                }
                            }
                        },
                        modifier =
                            when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                                else -> Modifier.fillMaxWidth(0.5f)
                            }
                                .height(56.dp)
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
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}