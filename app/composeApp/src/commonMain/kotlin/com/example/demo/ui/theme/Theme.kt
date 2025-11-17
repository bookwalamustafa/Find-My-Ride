package com.example.demo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun FindMyRideTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(),
        typography = MaterialTheme.typography,
        content = content,
    )
}

@Composable
fun lightColorScheme() = androidx.compose.material3.lightColorScheme(
    primary = DrexelGold,
    onPrimary = DrexelBlue,
    background = DrexelBlue,
    onBackground = Color.White,
)