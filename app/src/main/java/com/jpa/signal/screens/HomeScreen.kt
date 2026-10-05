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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.R
import com.jpa.signal.data.NavItem
import com.jpa.signal.ui.theme.app_gradient
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.neutral_tonal_light
import com.jpa.signal.ui.theme.primary_tonal_light
import com.jpa.signal.ui.theme.secondary_tonal_light
import com.jpa.signal.ui.theme.tertiary_light
import com.jpa.signal.ui.theme.tertiary_tonal_light

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGridApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController, auth: FirebaseAuth) {

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
                    title = { Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleLarge,
                        color = background_light
                    )},
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
                        ){
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
                                containerColor = neutral_tonal_light
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
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "Texto a Audio",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("audio-to-text") }),
                            colors = CardDefaults.cardColors(
                                containerColor = primary_tonal_light
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
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "Audio a Texto",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("find") }),
                            colors = CardDefaults.cardColors(
                                containerColor = secondary_tonal_light
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
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "Geolocalización",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.padding(16.dp).fillMaxSize().clickable(onClick = { navHostController.navigate("user-profile") }),
                            colors = CardDefaults.cardColors(
                                containerColor = tertiary_tonal_light
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
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = "Perfil de Usuario",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
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