package com.example.demo.feature.auth.forgot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.auth.AppLogo
import com.example.demo.feature.auth.AuthTextField
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@Composable
fun ForgotPasswordRoute(
    viewModel: ForgotPasswordViewModel = remember { ForgotPasswordViewModel() },
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ForgotPasswordScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordUiState,
    onEvent: (ForgotPasswordEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DrexelBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppLogo()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Reset Password",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Enter your email to receive a reset link.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(32.dp))

            AuthTextField(
                label = "Email",
                value = state.email,
                onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                placeholder = "your@email.com",
                isPassword = false
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onEvent(ForgotPasswordEvent.Submit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            ) {
                Text("Send Reset Link")
            }

            state.errorMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Red)
            }

            state.successMessage?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Green)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onNavigateBack) {
                Text("Back to Login", color = DrexelGold)
            }
        }
    }
}
