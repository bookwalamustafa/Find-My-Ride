package com.example.demo.feature.messages.data

import com.example.demo.feature.messages.chat.ChatMessageUi
import com.example.demo.feature.messages.list.MessageThreadUi

interface MessagesRepository {

    suspend fun getThreadsForUser(userId: Int): List<MessageThreadUi>

    suspend fun getMessagesForThread(threadId: Int): List<ChatMessageUi>

    suspend fun sendMessage(threadId: Int, text: String): ChatMessageUi
}