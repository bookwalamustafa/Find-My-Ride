package com.example.demo.feature.messages.list

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.demo.feature.messages.data.MessagesRepository

@Composable
fun MessagesListRoute(
    modifier: Modifier = Modifier,
    onOpenConversation: (MessageThreadUi) -> Unit,
    repository: MessagesRepository
) {
    // For now, we use userId = 1; AndroidMessagesRepository internally
    // will override this with CurrentUserStore.userId when available.
    val viewModel = remember(repository) {
        MessagesViewModel(
            repository = repository,
            userId = 1
        )
    }

    val state by viewModel.uiState.collectAsState()

    MessagesScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenConversation = onOpenConversation,
        modifier = modifier
    )
}
