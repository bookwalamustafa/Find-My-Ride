package com.example.demo.feature.messages.chat

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun ChatDetailRoute(
    threadId: Int,
    contactName: String,
    initials: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatDetailViewModel = remember {
        ChatDetailViewModel(threadId, contactName, initials)
    }
) {
    val state by viewModel.uiState.collectAsState()

    ChatDetailScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        modifier = modifier
    )
}
