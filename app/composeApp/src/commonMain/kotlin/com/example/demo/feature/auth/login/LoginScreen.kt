package com.example.demo.feature.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.auth.components.AppLogo
import com.example.demo.feature.auth.components.AuthScaffold
import com.example.demo.feature.auth.components.AuthTextField
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    AuthScaffold {
        AppLogo()

        Spacer(Modifier.height(32.dp))

        AuthTextField(
            label = "Email",
            value = state.email,
            onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
            placeholder = "your@email.com",
            isPassword = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AuthTextField(
            label = "Password",
            value = state.password,
            onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
            placeholder = "••••••••",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onNavigateToForgotPassword) {
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
            colors = ButtonDefaults.buttonColors(
                containerColor = DrexelGold,
                contentColor = DrexelBlue
            )
        ) {
            Text(if (state.isLoading) "Loading..." else "Log In")
        }

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

        TextButton(onClick = onNavigateToSignUp) {
            Text(
                text = "Sign up now",
                style = MaterialTheme.typography.bodyMedium,
                color = DrexelGold
            )
        }
    }
}
