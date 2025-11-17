package com.example.demo.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectAsState


// Routes (wires ViewModel to UI)

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

// Stateless UI Screen

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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppLogo()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Find My Ride",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun AppLogo() {
    TODO("Not yet implemented")
}