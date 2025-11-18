package com.example.demo.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

// ---------- Route (wires ViewModel to UI) ----------
@Composable
fun LoginRoute(
    viewModel: LoginViewModel = remember { LoginViewModel() }
) {
    val state by viewModel.uiState.collectAsState()

    LoginScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

// ---------- Stateless UI screen ----------

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginEvent) -> Unit
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
                text = "Find My Ride",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Share the journey, save the planet",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(Modifier.height(32.dp))

            AuthTextField(
                label = "Email",
                value = state.email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                placeholder = "your@email.com",
                isPassword = false
            )

            Spacer(Modifier.height(12.dp))

            AuthTextField(
                label = "Password",
                value = state.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                placeholder = "••••••••",
                isPassword = true
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { onEvent(LoginEvent.ForgotPassword) }) {
                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.labelMedium,
                        color = DrexelGold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onEvent(LoginEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            ) {
                Text(if (state.isLoading) "Loading..." else "Log In")
            }

            // Simple error text (optional)
            state.errorMessage?.let { error ->
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            TextButton(onClick = { onEvent(LoginEvent.SignUp) }) {
                Text(
                    text = "Sign up now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DrexelGold
                )
            }
        }
    }
}

