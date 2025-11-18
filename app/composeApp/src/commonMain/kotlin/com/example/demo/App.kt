package com.example.demo

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.demo.feature.auth.LoginRoute
import com.example.demo.feature.auth.LoginScreen

@Composable
fun App() {
    MaterialTheme {
        LoginRoute()
    }
}