package com.example.demo.feature.messages

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.demo.feature.messages.chat.ChatDetailRoute
import com.example.demo.feature.messages.list.MessageThreadUi
import com.example.demo.feature.messages.list.MessagesListRoute

private enum class MessagesPage { List, Conversation }

@Composable
fun MessagesRoute(
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(MessagesPage.List) }
    var activeThread by remember { mutableStateOf<MessageThreadUi?>(null) }

    when (currentPage) {
        MessagesPage.List -> MessagesListRoute(
            modifier = modifier,
            onOpenConversation = { thread ->
                activeThread = thread
                currentPage = MessagesPage.Conversation
            }
        )

        MessagesPage.Conversation -> {
            val thread = activeThread
            if (thread == null) {
                currentPage = MessagesPage.List
            } else {
                ChatDetailRoute(
                    threadId = thread.id,
                    contactName = thread.senderName,
                    initials = thread.initials,
                    onBack = { currentPage = MessagesPage.List },
                    modifier = modifier
                )
            }
        }
    }
}
