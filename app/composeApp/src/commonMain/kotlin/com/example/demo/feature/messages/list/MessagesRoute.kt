package com.example.demo.feature.messages.list

import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.demo.feature.messages.chat.ChatDetailScreen

enum class MessagesPage {
    List,
    Conversation
}


@Composable
fun MessagesRoute(
    viewModel: MessagesViewModel = remember { MessagesViewModel() },
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(MessagesPage.List) }
    var activeThread by remember { mutableStateOf<MessageItemUi?>(null) }
    val state by viewModel.uiState.collectAsState()

    when (currentPage) {
        MessagesPage.List -> MessagesScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onOpenConversation = { item ->
                activeThread = item
                currentPage = MessagesPage.Conversation
            },
            modifier = modifier
        )

        MessagesPage.Conversation -> {
            val thread = activeThread
            if (thread == null) {
                currentPage = MessagesPage.List
            } else {
                ChatDetailScreen(
                    contactName = thread.senderName,
                    initials = thread.initials,
                    onBack = { currentPage = MessagesPage.List },
                    modifier = modifier
                )
            }
        }
    }
}

