package com.jpa.signal.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jpa.signal.R
import com.jpa.signal.ui.theme.app_cta
import com.jpa.signal.ui.theme.app_gradient
import com.jpa.signal.ui.theme.background_light

@Composable
fun InitialScreen(navHostController: NavHostController){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = app_gradient,
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(2f))
        Image(
            painter = painterResource(id = R.drawable.logo_blanco),
            contentDescription = null,
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navHostController.navigate("sign-up")
            },
            modifier = when{
                currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.fillMaxWidth()
                else -> Modifier.fillMaxWidth(0.5f)
            }.padding(horizontal = 16.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = app_cta
            )
        ){
            Text(text = "Registrarse", style = MaterialTheme.typography.labelLarge, color = background_light)

        }

        Spacer(modifier = Modifier.weight(0.1f))
        TextButton(
            onClick = {
                navHostController.navigate("sign-in")
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Iniciar Sesión", style = MaterialTheme.typography.labelLarge, color = background_light)
        }
        Spacer(modifier = when{
            currentWindowAdaptiveInfoV2().windowSizeClass.minWidthDp <= 800 -> Modifier.weight(0.3f)
            else -> Modifier.weight(2f)
        })
    }
}