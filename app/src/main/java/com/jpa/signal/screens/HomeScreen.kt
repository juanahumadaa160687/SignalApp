package com.jpa.signal.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.jpa.signal.data.NavItem
import com.jpa.signal.data.Usuario
import com.jpa.signal.data.UsuarioRepo
import com.jpa.signal.ui.theme.app_gradient
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.neutral_tonal_light
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.primary_tonal_light
import com.jpa.signal.ui.theme.secondary_tonal_light
import com.jpa.signal.ui.theme.surface_light
import com.jpa.signal.ui.theme.tertiary_light
import com.jpa.signal.ui.theme.tertiary_tonal_light

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGridApi::class)

/*Pantalla Home
*
*  @input: navHostController: Controlador de navegación de Jetpack Compose y auth: Instancia de FirebaseAuth para autenticación.
*  @output: Pantalla home con botones para navegar a las pantallas de texto a audio, audio a texto, geolocalización y perfil de usuario.
*
*/
@Composable
fun HomeScreen(navHostController: NavHostController, auth: FirebaseAuth) {

    //Variable para el Scaffold que contiene la Navigation Suite para mostrar una barra de navegación de acuerdo con el tamaño de la pantalla
    val state = rememberNavigationSuiteScaffoldState()

    //Lista de items para la Navigation Suite
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


    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationSuiteType = when {
            //Barras de navegación de acuerdo con el tamaño de la pantalla
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
            //Se asigna cada item a la Navigation Suite
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
                        selectedTextColor = when{
                            currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> background_light
                            else -> primary_light
                        },
                        selectedIconColor = primary_light,
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
                    title = { Text(
                        text = "Home",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                        letterSpacing = 0.sp,
                        color = background_light
                    )},
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
                        ){
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

        ) {innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item{
                    Grid(
                        config =
                            {
                                repeat(2){
                                    column(200.dp)
                                }
                                repeat(2){
                                    row(200.dp)
                                }
                            },
                    ){
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("text-to-audio") }),
                            colors = CardDefaults.cardColors(
                                containerColor = surface_light
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { navHostController.navigate("text-to-audio") },
                                    modifier = Modifier.size(100.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_speaker),
                                        contentDescription = "Texto a Audio",
                                        modifier = Modifier.size(58.dp),
                                        tint = primary_light
                                    )
                                }
                                Text(
                                    text = "Texto a Audio",
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.1.sp,
                                    color = primary_light,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("audio-to-text") }),
                            colors = CardDefaults.cardColors(
                                containerColor = surface_light
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center

                            ) {
                                IconButton(
                                    onClick = { navHostController.navigate("audio-to-text") },
                                    modifier = Modifier.size(100.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_microphone),
                                        contentDescription = "Audio a Texto",
                                        modifier = Modifier.size(58.dp),
                                        tint = primary_light
                                    )
                                }
                                Text(
                                    text = "Audio a Texto",
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.1.sp,
                                    color = primary_light,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("find") }),
                            colors = CardDefaults.cardColors(
                                containerColor = surface_light
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { navHostController.navigate("find") },
                                    modifier = Modifier.size(100.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_maps),
                                        contentDescription = "Geolocalización",
                                        modifier = Modifier.size(58.dp),
                                        tint = primary_light
                                    )
                                }
                                Text(
                                    text = "Geolocalización",
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.1.sp,
                                    color = primary_light,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("user-profile") }),
                            colors = CardDefaults.cardColors(
                                containerColor = surface_light
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            ),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = { navHostController.navigate("user-profile") },
                                    modifier = Modifier.size(100.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_person),
                                        contentDescription = "Perfil de Usuario",
                                        modifier = Modifier.size(58.dp),
                                        tint = primary_light
                                    )
                                }
                                Text(
                                    text = "Perfil de Usuario",
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    letterSpacing = 0.1.sp,
                                    color = primary_light,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}