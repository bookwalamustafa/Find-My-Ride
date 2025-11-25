package com.example.demo.feature.messages.chat

sealed interface ChatDetailEvent {
    data class MessageTextChanged(val value: String) : ChatDetailEvent
    data object SendClicked : ChatDetailEvent
}
