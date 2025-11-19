package com.example.demo.feature.auth.forgot

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForgotPasswordViewModel {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun onEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged ->
                _uiState.value = _uiState.value.copy(
                    email = event.value,
                    errorMessage = null,
                    successMessage = null
                )

            ForgotPasswordEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val current = _uiState.value

        if (current.email.isBlank()) {
            _uiState.value = current.copy(errorMessage = "Please enter your email.")
            return
        }

        // Future: call backend to send reset email

        _uiState.value = current.copy(
            successMessage = "If an account exists, a reset link has been sent.",
            errorMessage = null
        )
    }
}
