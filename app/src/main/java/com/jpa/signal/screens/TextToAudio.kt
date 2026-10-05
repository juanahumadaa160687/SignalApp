package com.jpa.signal.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.data.NavItem
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.tertiary_light
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToAudio(navHostController: NavHostController, auth: FirebaseAuth){

    val context = LocalContext.current
    var textToSpeak by remember { mutableStateOf("") }

    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    var isInitialized by remember { mutableStateOf(false) }


    DisposableEffect(Unit) {
        /*
        * Para la correcta imprementación de la API de TextToSpeech, el emulador o el dispositivo final del usuario deben estar configurado con el idioma de preferencia del usuario
        */

        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
            }
        }
        val languages = tts.setLanguage(Locale.getDefault())
        if (languages == TextToSpeech.LANG_MISSING_DATA || languages == TextToSpeech.LANG_NOT_SUPPORTED) {
            isInitialized = false
        }

        textToSpeech = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val state = rememberNavigationSuiteScaffoldState()

    val navItems = listOf(
        NavItem(
            title = "Home",
            function = { navHostController.navigate("home") },
            icon = R.drawable.ic_home,
            route = "home"
        ),
        NavItem(
            title = "Voz a\nTexto",
            function = { navHostController.navigate("voice-to-text") },
            icon = R.drawable.ic_microphone,
            route = "voice-to-text"
        ),
        NavItem(
            title = "Texto a\nVoz",
            function = { navHostController.navigate("text-to-voice") },
            icon = R.drawable.ic_speaker,
            route = "text-to-voice"
        ),
        NavItem(
            title = "Salir",
            function = { auth.signOut(); navHostController.navigate("initial") },
            icon = R.drawable.ic_sign_out,
            route = ""
        )
    )


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
            navigationBarContainerColor = tertiary_light,
            navigationRailContainerColor = tertiary_light,
        ),
        navigationItems = {
            navItems.forEach { item ->
                NavigationSuiteItem(
                    label = {
                        Text(
                            text = item.title,
                            textAlign = TextAlign.Center,
                            style =
                                when {
                                    currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> MaterialTheme.typography.labelLarge
                                    else -> MaterialTheme.typography.labelMedium
                                },
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
                        selectedTextColor = when {
                            currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> background_light
                            else -> tertiary_light
                        },
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
                            text = "Texto a Voz",
                            style = MaterialTheme.typography.titleLarge,
                            color = background_light
                        )
                    },
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
                    },
                    actions = {
                        IconButton(
                            onClick = { navHostController.navigate("user-profile") }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = "Perfil de Usuario",
                                tint = background_light,
                                modifier = Modifier.requiredSize(24.dp)
                            )

                        }
                    }
                )
            }

        ) { innerPadding ->

            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically

                ) {TextField(
                    value = textToSpeak,
                    onValueChange = { textToSpeak = it },
                    placeholder = { Text("Ingrese el texto a convertir en audio") },
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
                    trailingIcon = {
                        IconButton(
                            onClick = { textToSpeak = "" },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                )}

                Spacer(modifier = Modifier.padding(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (isInitialized) {
                                textToSpeech?.speak(
                                    textToSpeak,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    null
                                )
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = primary_light
                        ),
                        modifier = Modifier.size(85.dp)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_speaker),
                            contentDescription = "Convertir Texto a Audio",
                            modifier = Modifier.size(58.dp),
                            tint = background_light
                        )
                    }
                }
            }
        }
    }
}