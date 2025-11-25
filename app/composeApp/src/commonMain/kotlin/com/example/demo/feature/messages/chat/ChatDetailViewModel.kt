package com.example.demo.feature.messages.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatDetailViewModel(
    private val threadId: Int,
    contactName: String,
    initials: String,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _uiState = MutableStateFlow(
        ChatDetailUiState(
            contactName = contactName,
            initials = initials,
            isLoading = true
        )
    )
    val uiState: StateFlow<ChatDetailUiState> = _uiState

    init {
        loadMessages()
    }

    private fun loadMessages() {
        scope.launch {
            // fake loading – later replace with repository
            _uiState.update {
                it.copy(
                    isLoading = false,
                    messages = listOf(
                        ChatMessageUi(1, false, "Hey!", "3h ago"),
                        ChatMessageUi(2, true, "Yo", "3h ago"),
                    )
                )
            }
        }
    }

    fun onEvent(event: ChatDetailEvent) {
        when (event) {
            is ChatDetailEvent.MessageTextChanged ->
                _uiState.update { it.copy(newMessageText = event.value) }

            ChatDetailEvent.SendClicked -> sendMessage()
        }
    }

    private fun sendMessage() {
        val text = _uiState.value.newMessageText.trim()
        if (text.isBlank()) return

        val newId = (_uiState.value.messages.maxOfOrNull { it.id } ?: 0) + 1

        _uiState.update {
            it.copy(
                messages = it.messages + ChatMessageUi(newId, true, text, "Now"),
                newMessageText = ""
            )
        }
    }
}
