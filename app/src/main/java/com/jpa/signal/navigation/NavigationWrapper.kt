package com.jpa.signal.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.data.SpeechRecognizerViewModel
import com.jpa.signal.screens.AudioToText
import com.jpa.signal.screens.EditProfileScreen
import com.jpa.signal.screens.FindMeScreen
import com.jpa.signal.screens.ForgotPasswordScreen
import com.jpa.signal.screens.InitialScreen
import com.jpa.signal.screens.SignInScreen
import com.jpa.signal.screens.SignUpScreen
import com.jpa.signal.screens.HomeScreen
import com.jpa.signal.screens.ResetPasswordScreen
import com.jpa.signal.screens.TextToAudio
import com.jpa.signal.screens.UserProfileScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationWrapper(navHostController: NavHostController, auth: FirebaseAuth) {

    NavHost(navHostController, startDestination = "initial") {
        composable("initial") {
            InitialScreen(navHostController)
        }
        composable("sign-in") {
            SignInScreen(navHostController, auth)
        }
        composable("sign-up") {
            SignUpScreen(navHostController, auth)
        }
        composable("home") {
            HomeScreen(navHostController, auth)
        }
        composable("forgot-password") {
            ForgotPasswordScreen(navHostController, auth)
        }
        composable("reset-password") {
            ResetPasswordScreen(navHostController, auth)
        }
        composable("user-profile") {
            UserProfileScreen(navHostController, auth)
        }
        composable("audio-to-text") {
            AudioToText(navHostController, auth)
        }
        composable("text-to-audio") {
            TextToAudio(navHostController, auth)
        }
        composable("find") {
            FindMeScreen(navHostController, auth)
        }
        composable("edit-profile") {
            EditProfileScreen(navHostController, auth)
        }
    }
}