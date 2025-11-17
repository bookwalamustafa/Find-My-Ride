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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey

@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

            // Yellow circle with car emoji (logo)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(DrexelGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🚗", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
            }

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

            // Email label
            Text(
                text = "Email",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("your@email.com", color = HintGrey) },
                singleLine = true,
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

            Spacer(Modifier.height(12.dp))

            // Password label
            Text(
                text = "Password",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White
            )
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("••••••••", color = HintGrey) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
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

            Spacer(Modifier.height(8.dp))

            // Forgot password (right aligned)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onForgotPasswordClick) {
                    Text(
                        text = "Forgot password?",
                        style = MaterialTheme.typography.labelMedium,
                        color = DrexelGold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Log In button
            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            ) {
                Text("Log In")
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Don't have an account?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Spacer(Modifier.height(4.dp))

            TextButton(onClick = onSignUpClick) {
                Text(
                    text = "Sign up now",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DrexelGold
                )
            }
        }
    }
}
