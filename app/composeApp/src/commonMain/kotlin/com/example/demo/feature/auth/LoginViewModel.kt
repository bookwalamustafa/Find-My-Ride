package com.example.demo.feature.auth

import kotlinx. coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    email = event.value,
                    errorMessage = null
                )
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    password = event.value,
                    errorMessage = null
                )
            }

            LoginEvent.Submit -> {
                // Will be a very basic validation for now
                val current = _uiState.value
                if (current.email.isBlank() || current.password.isBlank()) {
                    _uiState.value = current.copy(
                        errorMessage = "Please enter both a valid email and password"
                    )
                } else {
                    // Here is where you would call your auth API/repository
                    _uiState.value = current.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }

            LoginEvent.ForgotPassword -> {
                // Later: navigate or open reset flow
                // For now we just clear error
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }

            LoginEvent.SignUp -> {
                // Later: navigate to sign up screen
                _uiState.value = _uiState.value.copy(errorMessage = null)
            }
        }
    }
}