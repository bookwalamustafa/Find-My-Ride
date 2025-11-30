package com.example.demo.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey

@Composable
fun AppLogo() {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(DrexelGold, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("🚗", fontSize = MaterialTheme.typography.headlineMedium.fontSize)
    }
}

@Composable
fun AuthTextField(
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
