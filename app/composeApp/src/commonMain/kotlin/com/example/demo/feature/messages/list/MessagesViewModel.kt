package com.example.demo.feature.messages.list

import com.example.demo.feature.messages.data.MessagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MessagesViewModel(
    private val repository: MessagesRepository,
    private val userId: Int
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState

    init {
        loadThreads()
    }

    private fun loadThreads() {
        scope.launch {
            try {
                val threads = repository.getThreadsForUser(userId)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    threads = threads,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load messages"
                )
            }
        }
    }

    fun onEvent(event: MessagesEvent) {
        when (event) {
            is MessagesEvent.SearchQueryChanged ->
                _uiState.value = _uiState.value.copy(searchQuery = event.value)
        }
    }
}
