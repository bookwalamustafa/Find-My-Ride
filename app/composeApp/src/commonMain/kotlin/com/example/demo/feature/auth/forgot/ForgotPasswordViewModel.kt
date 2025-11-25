package com.example.demo.feature.auth.forgot

import FakeAuthRepository
import com.example.demo.feature.auth.data.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: AuthRepository = FakeAuthRepository()
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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
        val email = current.email.trim()

        // 1) Empty check
        if (email.isBlank()) {
            _uiState.value = current.copy(
                errorMessage = "Please enter your email.",
                successMessage = null
            )
            return
        }

        // 2) Very simple email validation (KMP-friendly)
        if (!isValidEmail(email)) {
            _uiState.value = current.copy(
                errorMessage = "Please enter a valid email address.",
                successMessage = null
            )
            return
        }

        // 3) Set loading state
        _uiState.value = current.copy(
            isLoading = true,
            errorMessage = null,
            successMessage = null
        )

        // 4) Call repository
        scope.launch {
            val result = repository.sendPasswordReset(email)

            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    successMessage = "If an account exists for $email, a reset link has been sent.",
                    errorMessage = null
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                        ?: "Could not send reset link. Please try again.",
                    successMessage = null
                )
            }
        }
    }

    // Simple, KMP-safe email validator (no Android APIs)
    private fun isValidEmail(email: String): Boolean {
        val atIndex = email.indexOf('@')
        if (atIndex <= 0 || atIndex == email.lastIndex) return false

        val dotIndex = email.lastIndexOf('.')
        if (dotIndex <= atIndex + 1 || dotIndex == email.lastIndex) return false

        return true
    }
}
