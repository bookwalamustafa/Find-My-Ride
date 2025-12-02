package com.example.demo.feature.messages.chat

import com.example.demo.feature.messages.data.MessagesRepository
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
    private val repository: MessagesRepository
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
            try {
                val msgs = repository.getMessagesForThread(threadId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        messages = msgs,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load messages"
                    )
                }
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

        scope.launch {
            try {
                val msg = repository.sendMessage(threadId, text)
                _uiState.update {
                    it.copy(
                        messages = it.messages + msg,
                        newMessageText = "",
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = e.message ?: "Failed to send message"
                    )
                }
            }
        }
    }
}
