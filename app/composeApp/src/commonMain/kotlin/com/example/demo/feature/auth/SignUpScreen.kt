package com.example.demo.feature.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle.Alignment
import androidx.compose.ui.unit.dp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel = remember { SignUpViewModel() },
    onNavigateToLogin: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    SignUpScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToLogin = onNavigateToLogin
    )
}

@Composable
fun SignUpScreen(
    state: SignUpUiState,
    onEvent: (SignUpEvent) -> Unit,
    onNavigateToLogin: () -> Unit
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
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
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
                value = state.name,
                onValueChange = { onEvent(SignUpEvent.NameChanged(it)) },
                placeholder = "Your Name",
                isPassword = false
            )

            Spacer(Modifier.height(12.dp))

            AuthTextField(
                label = "Email",
                value = state.email,
                onValueChange = { onEvent(SignUpEvent.EmailChanged(it)) },
                placeholder = "your@email.com",
                isPassword = false
            )

            Spacer(Modifier.height(12.dp))

            AuthTextField(
                label = "Password",
                value = state.password,
                onValueChange = { onEvent(SignUpEvent.PasswordChanged(it)) },
                placeholder = "Your Password",
                isPassword = true
            )

            Spacer(Modifier.height(12.dp))

            AuthTextField(
                label = "Confirm Password",
                value = state.password,
                onValueChange = { onEvent(SignUpEvent.PasswordChanged(it)) },
                placeholder = "Confirm Your Password",
                isPassword = true
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onEvent(SignUpEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth(),
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            )
        }
    }
}