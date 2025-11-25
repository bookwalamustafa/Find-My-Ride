package com.example.demo.feature.messages.list

import com.example.demo.feature.messages.list.MessagesEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MessagesViewModel {

    private val _uiState = MutableStateFlow(
        MessagesUiState(
            messages = listOf(
                MessageItemUi(
                    id = 1,
                    senderName = "Abdul B.",
                    initials = "AB",
                    lastMessage = "See you at 5:30 PM!",
                    timeAgo = "2m ago",
                    unreadCount = 2
                ),
                MessageItemUi(
                    id = 2,
                    senderName = "Sarah M.",
                    initials = "SM",
                    lastMessage = "Thanks for the ride yesterday",
                    timeAgo = "1h ago",
                    unreadCount = 0
                ),
                MessageItemUi(
                    id = 3,
                    senderName = "James K.",
                    initials = "JK",
                    lastMessage = "Is there still a seat available?",
                    timeAgo = "3h ago",
                    unreadCount = 1
                )
            )
        )
    )
    val uiState: StateFlow<MessagesUiState> = _uiState

    fun onEvent(event: MessagesEvent) {
        when (event) {
            is MessagesEvent.SearchQueryChanged -> {
                _uiState.value = _uiState.value.copy(searchQuery = event.value)

                // later: filter messages by query. For now we just store query.
            }

            is MessagesEvent.MessageClicked -> {
                // later: navigate to chat detail, mark read, etc.
                println("Message clicked: ${event.id}")
            }
        }
    }
}