package com.example.demo.feature.messages.chat

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.demo.feature.messages.data.MessagesRepository

@Composable
fun ChatDetailRoute(
    threadId: Int,
    contactName: String,
    initials: String,
    repository: MessagesRepository,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = remember(threadId, repository) {
        ChatDetailViewModel(threadId, contactName, initials, repository)
    }
    val state by viewModel.uiState.collectAsState()

    ChatDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}
