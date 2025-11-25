package com.example.demo.feature.messages.chat

data class ChatMessageUi(
    val id: Int,
    val isMe: Boolean,
    val text: String,
    val time: String
)

data class ChatDetailUiState(
    val contactName: String = "",
    val initials: String = "",
    val isLoading: Boolean = false,
    val messages: List<ChatMessageUi> = emptyList(),
    val newMessageText: String = "",
    val errorMessage: String? = null
)
