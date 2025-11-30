package com.example.demo.feature.auth.forgot

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    data object Submit : ForgotPasswordEvent
}
