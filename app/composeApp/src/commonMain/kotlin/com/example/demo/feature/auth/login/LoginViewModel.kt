package com.example.demo.feature.auth.login

import com.example.demo.feature.auth.data.AuthRepository
import com.example.demo.feature.auth.data.FakeAuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// real app ready: async + error handling
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

        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Email and password are required")
            return
        }

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        scope.launch {
            val result = repository.login(state.email, state.password)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                // later: trigger navigation to main app screen
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message ?: "Login failed"
                )
            }
        }
    }
}
