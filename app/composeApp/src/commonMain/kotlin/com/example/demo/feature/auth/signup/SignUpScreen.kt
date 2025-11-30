package com.example.demo.feature.auth.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.auth.components.AppLogo
import com.example.demo.feature.auth.components.AuthScaffold
import com.example.demo.feature.auth.components.AuthTextField
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@Composable
fun SignUpScreen(
    state: SignUpUiState,
    onEvent: (SignUpEvent) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    AuthScaffold {
        AppLogo()

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Join the ride-sharing community",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(Modifier.height(32.dp))

        AuthTextField(
            label = "Name",
            value = state.fullName,
            onValueChange = { onEvent(SignUpEvent.NameChanged(it)) },
            placeholder = "Your name",
            isPassword = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AuthTextField(
            label = "Email",
            value = state.email,
            onValueChange = { onEvent(SignUpEvent.EmailChanged(it)) },
            placeholder = "your@email.com",
            isPassword = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AuthTextField(
            label = "Password",
            value = state.password,
            onValueChange = { onEvent(SignUpEvent.PasswordChanged(it)) },
            placeholder = "••••••••",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        AuthTextField(
            label = "Confirm Password",
            value = state.confirmPassword,
            onValueChange = { onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
            placeholder = "••••••••",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onEvent(SignUpEvent.Submit) },
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
            Text(if (state.isLoading) "Signing up..." else "Sign Up")
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
            text = "Already have an account?",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )

        Spacer(Modifier.height(4.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "Log in",
                style = MaterialTheme.typography.bodyMedium,
                color = DrexelGold
            )
        }
    }
}
