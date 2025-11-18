package com.example.demo.feature.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged ->
                _uiState.value = _uiState.value.copy(name = event.value, errorMessage = null)

            is SignUpEvent.EmailChanged ->
                _uiState.value = _uiState.value.copy(email = event.value, errorMessage = null)

            is SignUpEvent.PasswordChanged ->
                _uiState.value = _uiState.value.copy(password = event.value, errorMessage = null)

            is SignUpEvent.ConfirmPasswordChanged ->
                _uiState.value = _uiState.value.copy(confirmPassword = event.value, errorMessage = null)

            SignUpEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val current = _uiState.value

        if (current.name.isBlank() ||
            current.email.isBlank() ||
            current.password.isBlank() ||
            current.confirmPassword.isBlank()
        ) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please fill in all fields!")
            return
        }

        if (current.password != current.confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords don't match!")
            return
        }

        // sign up api later add form Drexel
        _uiState.value = current.copy(
            isLoading = true,
            errorMessage = null
        )
    }
}