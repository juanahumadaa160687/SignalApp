package com.jpa.signal

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.jpa.signal.navigation.NavigationWrapper
import com.jpa.signal.ui.theme.SignalTheme
import com.jpa.signal.ui.theme.background_light

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth
        setContent {

            navHostController = rememberNavController()

            SignalTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = background_light
                ) {
                    NavigationWrapper(navHostController, auth)
                }
            }
        }
    }

    //Función que se ejecuta al iniciar la actividad, comprueba si el usuario está autenticado, si lo está, cierra la sesión.
    override fun onStart(){
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            auth.signOut()
        }
    }
}
