package com.jpa.signal.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.auth.FirebaseAuth
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.jpa.signal.R
import com.jpa.signal.data.NavItem
import com.jpa.signal.ui.theme.background_light
import com.jpa.signal.ui.theme.tertiary_light
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindMe(navHostController: NavHostController, auth: FirebaseAuth){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember{ LocationServices.getFusedLocationProviderClient(context) }

    var userLatLng by remember {mutableStateOf<LatLng?>(null)}

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-20.21492183013291, -70.13583108234238), 20f)
    }

    var markerState by remember { mutableStateOf<MarkerState?>(null) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    fun updateMapCamera(latLng: LatLng){
        userLatLng = latLng
        markerState = MarkerState(position = latLng)
        scope.launch {
          cameraPositionState.animate(
              CameraUpdateFactory.newLatLngZoom(latLng, 10f)
          )
        }
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        hasLocationPermission = isGranted
        if(isGranted){
            try {
                fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).addOnSuccessListener { location: Location? ->
                    location?.let {
                        updateMapCamera(LatLng(it.latitude, it.longitude))
                    }
                }
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

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

    val state = rememberNavigationSuiteScaffoldState()

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
                            text = "Perfil de Usuario",
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

        ) {innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            isMyLocationEnabled = hasLocationPermission
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = true,
                            myLocationButtonEnabled = true,
                            mapToolbarEnabled = true,
                            compassEnabled = true
                        )
                    ) {
                        markerState?.let { state ->
                            Marker(
                                state = state,
                                title = "Mi ubicación",
                                snippet = "Aquí estoy: ${userLatLng?.latitude}, ${userLatLng?.longitude}",
                                draggable = true,
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker),
                                visible = true
                            )
                        }
                    }
                }
            }
        }
    }
}