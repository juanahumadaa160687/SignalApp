package com.jpa.signal.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jpa.signal.R
import com.jpa.signal.ui.theme.app_cta
import com.jpa.signal.ui.theme.app_gradient

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
fun InitialScreen(navHostController: NavHostController){
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
                onClick = { /*TODO: Continuar con Google*/ },
                modifier = when{
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
                    containerColor = Color.Black),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Continuar con Google",
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