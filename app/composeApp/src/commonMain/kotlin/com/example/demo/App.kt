package com.example.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.demo.feature.auth.login.LoginRoute
import com.example.demo.feature.auth.signup.SignUpRoute
import com.example.demo.feature.auth.forgot.ForgotPasswordRoute
import com.example.demo.feature.main.MainRoute
import com.example.demo.feature.profile.ProfileRoute

// Top-level screens in your app
enum class RootScreen {
    Login,
    SignUp,
    ForgotPassword,
    Main
}

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(RootScreen.Login) }

    MaterialTheme {
        when (currentScreen) {

            RootScreen.Login -> LoginRoute(
                onNavigateToSignUp = { currentScreen = RootScreen.SignUp },
                onNavigateToForgotPassword = { currentScreen = RootScreen.ForgotPassword },
                onLoginSuccess = { currentScreen = RootScreen.Main }
            )

            RootScreen.SignUp -> SignUpRoute(
                onNavigateToLogin = { currentScreen = RootScreen.Login }
            )

            RootScreen.ForgotPassword -> ForgotPasswordRoute(
                onNavigateBack = { currentScreen = RootScreen.Login }
            )

            RootScreen.Main -> MainRoute()
        }
    }
}
