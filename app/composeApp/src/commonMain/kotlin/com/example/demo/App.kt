package com.example.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.demo.feature.auth.forgot.ForgotPasswordRoute
import com.example.demo.feature.auth.login.LoginRoute
import com.example.demo.feature.auth.signup.SignUpRoute

enum class AuthScreen { Login, SignUp, ForgotPassword }
@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(AuthScreen.Login) }

    MaterialTheme {
        when (currentScreen) {
            AuthScreen.Login -> LoginRoute(
                onNavigateToSignUp = { currentScreen = AuthScreen.SignUp },
                onNavigateToForgotPassword = { currentScreen = AuthScreen.ForgotPassword }
            )
            AuthScreen.SignUp -> SignUpRoute(
                onNavigateToLogin = { currentScreen = AuthScreen.Login }
            )
            AuthScreen.ForgotPassword -> ForgotPasswordRoute(
                onNavigateBack = { currentScreen = AuthScreen.Login }
            )
        }
    }
}