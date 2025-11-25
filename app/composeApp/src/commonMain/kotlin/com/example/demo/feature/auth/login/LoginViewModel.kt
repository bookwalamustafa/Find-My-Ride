package com.example.demo.feature.auth.login

import FakeAuthRepository
import com.example.demo.feature.auth.data.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = FakeAuthRepository()
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged ->
                _uiState.value = _uiState.value.copy(email = event.value, errorMessage = null)

            is LoginEvent.PasswordChanged ->
                _uiState.value = _uiState.value.copy(password = event.value, errorMessage = null)

            LoginEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value
        val email = state.email.trim()
        val password = state.password

        // Required fields
        if (email.isBlank() || password.isBlank()) {
            return setError("Email and password are required.")
        }

        // Basic email validation
        if (!isValidEmail(email)) {
            return setError("Please enter a valid email address.")
        }

        _uiState.value = state.copy(
            isLoading = true,
            errorMessage = null
        )

        scope.launch {
            val result = repository.login(email, password)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed."
                )
            }
        }
    }

    private fun setError(message: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = message,
            isLoading = false
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val at = email.indexOf('@')
        val dot = email.lastIndexOf('.')
        return at > 0 && dot > at + 1 && dot < email.length - 1
    }
}

