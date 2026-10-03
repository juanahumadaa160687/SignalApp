package com.jpa.signal.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import com.jpa.signal.screens.ForgotPasswordScreen
import com.jpa.signal.screens.InitialScreen
import com.jpa.signal.screens.SignInScreen
import com.jpa.signal.screens.SignUpScreen
import com.jpa.signal.screens.HomeScreen
import com.jpa.signal.screens.ResetPasswordScreen
import com.jpa.signal.screens.UserProfile

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
            HomeScreen(navHostController)
        }
        composable("forgot-password") {
            ForgotPasswordScreen(navHostController)
        }
        composable("reset-password") {
            ResetPasswordScreen(navHostController)
        }
        composable("user-profile") {
            UserProfile()
        }

    }
}