package com.example.demo.feature.messages.data

import com.example.demo.feature.messages.chat.ChatMessageUi
import com.example.demo.feature.messages.list.MessageThreadUi
import kotlinx.coroutines.delay

class FakeMessagesRepository : MessagesRepository {

    // pretend current user is id=1
    private val fakeThreads = mutableListOf(
        MessageThreadUi(
            id = 1,
            senderName = "Alex Johnson",
            initials = "AJ",
            lastMessage = "Are we still on for 5:30 PM?",
            timeAgo = "2m ago",
            unreadCount = 1
        ),
        MessageThreadUi(
            id = 2,
            senderName = "Campus Carpool",
            initials = "CC",
            lastMessage = "Thanks again for organizing the rides!",
            timeAgo = "3h ago",
            unreadCount = 0
        ),
        MessageThreadUi(
            id = 3,
            senderName = "Taylor Smith",
            initials = "TS",
            lastMessage = "I’ll grab the front seat next time 😄",
            timeAgo = "Yesterday",
            unreadCount = 0
        )
    )

    // in-memory messages per thread
    private val fakeMessages = mutableMapOf(
        1 to mutableListOf(
            ChatMessageUi(1, isMe = false, "Hey, thanks for the ride!", "3h ago"),
            ChatMessageUi(2, isMe = true, "Of course! Happy to help.", "3h ago"),
            ChatMessageUi(3, isMe = false, "Are we still on for 5:30 PM?", "2m ago"),
        ),
        2 to mutableListOf(
            ChatMessageUi(4, isMe = false, "Carpool this Friday?", "1d ago"),
            ChatMessageUi(5, isMe = true, "Yes, let’s do it!", "1d ago"),
        )
    )

    override suspend fun getThreadsForUser(userId: Int): List<MessageThreadUi> {
        delay(200) // fake network/db delay
        return fakeThreads
    }

    override suspend fun getMessagesForThread(threadId: Int): List<ChatMessageUi> {
        delay(150)
        return fakeMessages[threadId]?.toList() ?: emptyList()
    }

    override suspend fun sendMessage(threadId: Int, text: String): ChatMessageUi {
        delay(100)
        val list = fakeMessages.getOrPut(threadId) { mutableListOf() }
        val newId = (fakeMessages.values.flatten().maxOfOrNull { it.id } ?: 0) + 1
        val msg = ChatMessageUi(
            id = newId,
            isMe = true,
            text = text,
            time = "Now"
        )
        list.add(msg)

        // update last message in thread
        val idx = fakeThreads.indexOfFirst { it.id == threadId }
        if (idx >= 0) {
            val thread = fakeThreads[idx]
            fakeThreads[idx] = thread.copy(lastMessage = text, timeAgo = "Now")
        }

        return msg
    }
}
