package com.example.demo.feature.messages.list

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.demo.feature.messages.data.FakeMessagesRepository
import com.example.demo.feature.messages.data.MessagesRepository

@Composable
fun MessagesListRoute(
    modifier: Modifier = Modifier,
    onOpenConversation: (MessageThreadUi) -> Unit,
    repository: MessagesRepository = FakeMessagesRepository()
) {
    val viewModel = remember(repository) {
        // userId is still 1 here; Android repo will ignore it and use CurrentUserStore
        MessagesViewModel(repository = repository, userId = 1)
    }

    val state by viewModel.uiState.collectAsState()

    MessagesScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onOpenConversation = onOpenConversation,
        modifier = modifier
    )
}
