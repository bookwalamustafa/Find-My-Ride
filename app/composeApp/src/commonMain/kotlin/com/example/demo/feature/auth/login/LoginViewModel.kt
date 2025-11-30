package com.example.demo.feature.auth.login

import com.example.demo.feature.auth.data.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _uiState.value = _uiState.value.copy(
                    email = event.value,
                    errorMessage = null,
                    loginSuccess = false
                )
            }

            is LoginEvent.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(
                    password = event.value,
                    errorMessage = null,
                    loginSuccess = false
                )
            }

            LoginEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = _uiState.value

        if (!isValidEmail(state.email)) {
            setError("Please enter a valid email")
            return
        }
        if (state.password.isBlank()) {
            setError("Password is required")
            return
        }

        // start loading
        _uiState.value = state.copy(
            isLoading = true,
            errorMessage = null,
            loginSuccess = false
        )

        scope.launch {
            val result = repository.login(state.email.trim(), state.password)

            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true,
                    errorMessage = null
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }

    private fun setError(message: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = message,
            isLoading = false,
            loginSuccess = false
        )
    }

    // Same simple cross-platform email validation you used before
    private fun isValidEmail(email: String): Boolean {
        val at = email.indexOf('@')
        val dot = email.lastIndexOf('.')
        return at > 0 && dot > at + 1 && dot < email.length - 1
    }
}
