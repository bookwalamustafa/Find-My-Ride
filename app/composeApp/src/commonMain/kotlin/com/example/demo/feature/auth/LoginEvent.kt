package com.example.demo.feature.auth


// Contract of what the user can do on the screen.
sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object Submit : LoginEvent
    data object ForgotPassword : LoginEvent
    data object SignUp : LoginEvent
}