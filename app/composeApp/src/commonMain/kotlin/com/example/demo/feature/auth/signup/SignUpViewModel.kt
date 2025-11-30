package com.example.demo.feature.auth.signup

import com.example.demo.feature.auth.data.AuthRepository
import com.example.demo.feature.auth.data.FakeAuthRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel(
    private val repository: AuthRepository = FakeAuthRepository()
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.NameChanged ->
                _uiState.value = _uiState.value.copy(fullName = event.value, errorMessage = null)

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
        val state = _uiState.value
        val name = state.fullName.trim()
        val email = state.email.trim()
        val pass = state.password
        val confirm = state.confirmPassword

        // 1) Require all fields
        if (name.isBlank() || email.isBlank() || pass.isBlank() || confirm.isBlank()) {
            setError("Please fill in all fields.")
            return
        }

        // 2) Email validation
        if (!isValidEmail(email)) {
            setError("Please enter a valid email.")
            return
        }

        // 3) Password match
        if (pass != confirm) {
            setError("Passwords do not match.")
            return
        }

        // 4) Password strength
        if (pass.length < 6) {
            setError("Password must be at least 6 characters.")
            return
        }

        // Clear old errors + show loading
        _uiState.value = state.copy(
            isLoading = true,
            errorMessage = null
        )

        // 5) Call repository
        scope.launch {
            val result = repository.signUp(name, email, pass)

            _uiState.value = if (result.isSuccess) {
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    success = true // you can use this to auto-navigate later
                )
            } else {
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.exceptionOrNull()?.message
                        ?: "Sign up failed. Try again."
                )
            }
        }
    }

    private fun setError(msg: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = msg,
            isLoading = false
        )
    }

    // Simple cross-platform email validation
    private fun isValidEmail(email: String): Boolean {
        val at = email.indexOf('@')
        val dot = email.lastIndexOf('.')
        return at > 0 && dot > at + 1 && dot < email.length - 1
    }
}
