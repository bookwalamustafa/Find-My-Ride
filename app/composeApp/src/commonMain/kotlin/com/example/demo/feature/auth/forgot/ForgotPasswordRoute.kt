package com.example.demo.feature.auth.forgot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun ForgotPasswordRoute(
    onNavigateBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = remember { ForgotPasswordViewModel() }
) {
    val state by viewModel.uiState.collectAsState()

    ForgotPasswordScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}
