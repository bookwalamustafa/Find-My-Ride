package com.example.demo.feature.messages.list

sealed interface MessagesEvent {
    data class SearchQueryChanged(val value: String) : MessagesEvent
}
