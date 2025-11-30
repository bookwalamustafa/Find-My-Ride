package com.example.demo.feature.messages.list

data class MessageThreadUi(
    val id: Int,
    val senderName: String,
    val initials: String,
    val lastMessage: String,
    val timeAgo: String,
    val unreadCount: Int = 0
)

data class MessagesUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val threads: List<MessageThreadUi> = emptyList(),
    val errorMessage: String? = null
)
