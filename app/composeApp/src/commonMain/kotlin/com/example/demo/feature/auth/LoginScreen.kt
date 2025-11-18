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
import androidx.compose.ui.text.Placeholder
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey
import kotlinx.coroutines.flow.StateFlow


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
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(DrexelGold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("🏎️", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
    }
}

@Composable
private fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = Color.White
    )
    Spacer(Modifier.height(4.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = HintGrey) },
        singleLine = true,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedContainerColor = FieldBackground,
            unfocusedContainerColor = FieldBackground,
            cursorColor = DrexelGold,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        )
    )
}