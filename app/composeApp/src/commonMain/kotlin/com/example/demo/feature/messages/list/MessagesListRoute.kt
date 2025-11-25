package com.example.demo.feature.messages.list

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun MessagesListRoute(
    modifier: Modifier = Modifier,
    onOpenConversation: (MessageThreadUi) -> Unit,
    viewModel: MessagesViewModel = remember { MessagesViewModel() }
) {
    val state by viewModel.uiState.collectAsState()

    MessagesScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenConversation = onOpenConversation,
        modifier = modifier
    )
}
