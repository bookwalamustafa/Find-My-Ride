package com.example.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.demo.feature.auth.LoginRoute
import com.example.demo.feature.auth.LoginScreen
import com.example.demo.feature.auth.SignUpRoute

enum class AuthScreen { Login, SignUp }
@Composable
fun App() {
    var currentScreen by remember { mutableStateOf(AuthScreen.Login) }

    MaterialTheme {
        when (currentScreen) {
            AuthScreen.Login -> LoginRoute(
                onNavigateToSignUp = { currentScreen = AuthScreen.SignUp }
            )
            AuthScreen.SignUp -> SignUpRoute(
                onNavigateToLogin = { currentScreen = AuthScreen.Login }
            )
        }
    }
}