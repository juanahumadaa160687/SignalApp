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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationItemColors
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.jpa.signal.data.NavItem
import com.jpa.signal.data.Usuario
import com.jpa.signal.data.UsuarioRepo
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.surface_light
import com.jpa.signal.ui.theme.tertiary_light
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
/*Pantalla de edición de perfil de usuario
*
*  @input: navHostController: Controlador de navegación de Jetpack Compose y auth: Instancia de FirebaseAuth para autenticación.
*  @output: Pantalla de edición de perfil de usuario con campos de texto para el nombre, apellido, edad, teléfono y dirección.
*
*/
@Composable
fun EditProfileScreen(navHostController: NavHostController, auth: FirebaseAuth) {

    //Variables para obtener el usuario actual
    val currentUser = auth.currentUser
    val scope = rememberCoroutineScope()
    val userUid = currentUser?.uid
    var editedUsuario: Usuario by remember { mutableStateOf( Usuario()) }

    //Obtener el usuario actual
    LaunchedEffect(Unit) {
        val usuario = UsuarioRepo.getUsuarioByUid(userUid!!)
        if (usuario != null) {
            editedUsuario = usuario
        }
    }

    //Variables para los campos de texto
    var rut by remember { mutableStateOf(editedUsuario.rut) }
    var nombre by remember { mutableStateOf(editedUsuario.nombre) }
    var apellido by remember { mutableStateOf(editedUsuario.apellido) }
    var edad by remember { mutableStateOf(editedUsuario.edad) }
    var telefono by remember { mutableStateOf(editedUsuario.telefono) }
    var direccion by remember { mutableStateOf(editedUsuario.direccion) }

    val navItems = listOf(
        NavItem(
            title = "Home",
            function = { navHostController.navigate("home") },
            icon = R.drawable.ic_home,
            route = "home"
        ),
        NavItem(
            title = "Voz a Texto",
            function = { navHostController.navigate("audio-to-text") },
            icon = R.drawable.ic_microphone,
            route = "audio-to-text"
        ),
        NavItem(
            title = "Texto a Voz",
            function = { navHostController.navigate("text-to-audio") },
            icon = R.drawable.ic_speaker,
            route = "text-to-audio"
        ),
        NavItem(
            title = "Salir",
            function = { auth.signOut(); navHostController.navigate("initial") },
            icon = R.drawable.ic_sign_out,
            route = ""
        )
    )

    val state = rememberNavigationSuiteScaffoldState()

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
                    Text(
                        "Error al actualizar el perfil",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp,
                        color = tertiary_light
                    )
                },
                text = {
                    Text(
                        text = text,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center,
                        color = primary_light
                    )
                },
                confirmButton = {
                    TextButton(onClick = { openAlertDialog.value = false }) {
                        Text("Aceptar")
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_error),
                        contentDescription = null,
                        modifier = Modifier.requiredSize(32.dp)
                    )
                },
                containerColor = surface_light,
                textContentColor = primary_light,
                titleContentColor = tertiary_light,
            )
        }
    }

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteType = when {
            currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.NavigationRail
        },
        state = state,
        primaryActionContentHorizontalAlignment = Alignment.Start,
        navigationItemVerticalArrangement = Arrangement.Center,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = primary_light,
            navigationRailContainerColor = primary_light,
        ),
        navigationItems = {
            navItems.forEach { item ->
                NavigationSuiteItem(
                    label = {
                        Text(
                            text = item.title,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            lineHeight = 20.sp,
                            letterSpacing = 0.1.sp
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = "Ir a ${item.title}",
                            modifier = when {
                                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.size(24.dp)
                                else -> Modifier.size(20.dp)
                            },
                        )
                    },
                    onClick = {
                        item.function()
                    },
                    selected = navHostController.currentDestination?.route == item.route,
                    colors = NavigationItemColors(
                        selectedTextColor = background_light,
                        selectedIconColor = tertiary_light,
                        unselectedTextColor = background_light,
                        unselectedIconColor = background_light,
                        selectedIndicatorColor = background_light,
                        disabledTextColor = background_light,
                        disabledIconColor = background_light,
                    ),
                    modifier = when {
                        currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.padding(top = 12.dp)
                        else -> Modifier.padding(top = 4.dp, bottom = 4.dp)
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = background_light,

            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Perfil de Usuario",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp,
                            color = background_light,
                        )
                    },
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
                    },
                    actions = {
                        IconButton(
                            onClick = { navHostController.navigate("user-profile") }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "Perfil de Usuario",
                                tint = background_light,
                                modifier = Modifier.requiredSize(32.dp)
                            )

                        }
                    }
                )
            }

        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Spacer(modifier = Modifier.height(40.dp))

                        TextField(
                            value = editedUsuario.rut,
                            onValueChange = { editedUsuario = editedUsuario.copy(rut = it) },
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
                                    onClick = { rut = "" },
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
                            value = editedUsuario.nombre,
                            onValueChange = { editedUsuario = editedUsuario.copy(nombre = it) },
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
                                    onClick = { nombre = "" },
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
                            value = editedUsuario.apellido,
                            onValueChange = { editedUsuario = editedUsuario.copy(apellido = it) },
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
                                    onClick = { apellido = "" },
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
                            value = editedUsuario.edad,
                            onValueChange = { editedUsuario = editedUsuario.copy(edad = it) },
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
                                    onClick = { edad = "" },
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
                            value = editedUsuario.telefono,
                            onValueChange = { editedUsuario = editedUsuario.copy(telefono = it) },
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
                                    onClick = { telefono = "" },
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
                            value = editedUsuario.direccion,
                            onValueChange = { editedUsuario = editedUsuario.copy(direccion = it) },
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
                                    onClick = { direccion = "" },
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
                                        colors = RadioButtonDefaults.colors(
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
                        Button(
                            onClick = {
                                scope.launch {
                                UsuarioRepo.updateUsuario(userUid!!, editedUsuario)
                                navHostController.navigate("user-profile")
                            } },
                            modifier = Modifier.padding(16.dp).fillMaxWidth().height(56.dp)
                        ){
                            Text(
                                text = "Guardar",
                            )
                        }
                    }
                }
            }
        }
    }
}