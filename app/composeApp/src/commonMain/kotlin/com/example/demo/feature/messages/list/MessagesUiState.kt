package com.example.demo.feature.messages.list

data class MessageItemUi(
    val id: Int,
    val senderName: String,
    val initials: String,
    val lastMessage: String,
    val timeAgo: String,
    val unreadCount: Int,
)

data class MessagesUiState(
    val searchQuery: String = "",
    val messages: List<MessageItemUi> = emptyList()
)