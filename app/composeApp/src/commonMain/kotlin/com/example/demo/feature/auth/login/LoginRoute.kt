package com.example.demo.feature.auth.login

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState

@Composable
fun LoginRoute(
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = remember { LoginViewModel() }
) {
    val state by viewModel.uiState.collectAsState()

    // When login succeeds, trigger navigation once
    LaunchedEffect(state.loginSuccess) {
        if (state.loginSuccess) {
            onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToSignUp = onNavigateToSignUp,
        onNavigateToForgotPassword = onNavigateToForgotPassword
    )
}
