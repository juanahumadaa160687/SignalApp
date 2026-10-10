package com.jpa.signal.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jpa.signal.R
import com.jpa.signal.data.Usuario
import com.jpa.signal.data.UsuarioRepo
import com.jpa.signal.ui.theme.app_cta
import com.jpa.signal.ui.theme.app_gradient
import com.jpa.signal.ui.theme.primary_light
import com.jpa.signal.ui.theme.surface_light
import com.jpa.signal.ui.theme.tertiary_light
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/****************Bienvenido a Signl App*****************/

/* La aplicación esta pensada en ser una plataforma de
 comunicación segura y cómoda para los usuarios con
 algún grado de discapacidad auditiva.

 Se compone de 3 servicios, los cuales son:
    - Servicio de Voz a Texto, el cual permite a los
    usuarios recibir mensajes de voz  y transformarlos
    en texto.

    - Servicio de Texto a Voz, el que permite al usuario
    escribir un mensaje y transformarlo en voz. Además de
    utilizar frases de voz predefinidas.

    - Servicio de Geolocalización, el cual permite a los
    usuarios revisar su localización y ver los servicios
    cercanos.

    Se adapta a una variedad de dispositivos y pantallas.

    Permite iniciar sesión a través de los servicios de
    inicio de sesión con correo y contraseña y a través
    de Google Auth.
*/


@Composable
fun InitialScreen(navHostController: NavHostController, auth: FirebaseAuth){

    var usuario: Usuario by remember { mutableStateOf(Usuario()) }

    val scope = rememberCoroutineScope()

    var text by remember { mutableStateOf("") }

    var openAlertDialog = remember { mutableStateOf(false) }

    //Si la variable openAlertDialog es true, se muestra el AlertDialog
    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                title = {
                    when {
                        text == "Credencial no válida" || text == "Error al obtener credenciales" ->
                            Text(
                                text = "Oops... algo anda mal",
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Normal,
                                fontSize = 22.sp,
                                lineHeight = 28.sp,
                                letterSpacing = 0.sp,
                                color = primary_light
                            )
                        else -> Text(
                            text = "Inicio de Sesión Exitoso",
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 22.sp,
                            lineHeight = 28.sp,
                            letterSpacing = 0.sp,
                            color = primary_light
                        )
                    }
                },
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
                    TextButton(onClick = {
                        when {
                            text == "Error al obtener credenciales" || text == "Credencial no válida" -> openAlertDialog.value = false
                        }
                    }) {
                        when {
                            text == "Error al obtener credenciales" || text == "Credencial no válida" -> Text("Reintentar")
                            else -> Text("Ingresar")
                        }
                    }
                },
                icon = { Icon(painter = painterResource(id = R.drawable.ic_error), contentDescription = null, modifier = Modifier.requiredSize(32.dp)) },
                containerColor = surface_light,
                textContentColor = primary_light,
                titleContentColor = tertiary_light,
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = app_gradient
                )),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        item{
            Image(
                painter = painterResource(id = R.drawable.logo_blanco),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            Text(
                text = "Bienvenido a Signl App",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
        item {
            Button(
                onClick = {
                    navHostController.navigate("sign-up")
                },
                modifier = when {
                    currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)

                    else -> Modifier
                        .height(48.dp)
                        .fillMaxWidth(0.5f)
                        .padding(horizontal = 32.dp)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = app_cta
                )
            ) {
                Text(
                    text = "Registrate Gratis",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }

        item{
            Button(
                onClick = {
                    val credential = GoogleAuthProvider.getCredential(R.string.default_web_client_id.toString(), null)

                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                scope.launch {
                                    usuario = UsuarioRepo.getUsuarioByUid(auth.currentUser!!.uid)!!

                                    if (usuario != null) {
                                        text = "Bienvenido ${usuario.nombre}"
                                        openAlertDialog.value = true
                                        navHostController.navigate("home")
                                    } else {
                                        navHostController.navigate("sign-up-google")
                                    }
                                }
                            }else{
                                openAlertDialog.value = true
                                text = "Error al obtener credenciales"
                            }
                        }


                },
                modifier = when {
                    currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)

                    else -> Modifier
                        .height(48.dp)
                        .fillMaxWidth(0.5f)
                        .padding(horizontal = 32.dp)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = null
                )

                Text(
                    text = "Iniciar Sesion con Google",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }

        item{
            TextButton(
                onClick = { navHostController.navigate("sign-in") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Iniciar Sesion",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }

    }
}