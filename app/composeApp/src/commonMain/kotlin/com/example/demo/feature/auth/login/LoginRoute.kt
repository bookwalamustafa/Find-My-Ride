package com.example.demo.feature.auth.login

import FakeAuthRepository
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import com.example.demo.feature.auth.data.AuthRepository

@Composable
fun LoginRoute(
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    authRepository: AuthRepository
) {
    val viewModel = remember(authRepository) {
        LoginViewModel(authRepository)
    }
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
