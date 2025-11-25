package com.example.demo.feature.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun SignUpRoute(
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = remember { SignUpViewModel() }
) {
    val state by viewModel.uiState.collectAsState()

    SignUpScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin
    )
}
