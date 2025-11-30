package com.example.demo.feature.messages.data

import com.example.demo.CurrentUserStore
import com.example.demo.FindMyRideDbProvider
import com.example.demo.feature.messages.chat.ChatMessageUi
import com.example.demo.feature.messages.list.MessageThreadUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidMessagesRepository(
    private val dbProvider: FindMyRideDbProvider
) : MessagesRepository {

    /**
     * Use logged-in user if available, otherwise fall back to parameter.
     */
    private fun resolveCurrentUserId(paramUserId: Int): Int {
        val stored = CurrentUserStore.userId
        return stored?.toInt() ?: paramUserId
    }

    override suspend fun getThreadsForUser(userId: Int): List<MessageThreadUi> =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getReadableDatabase()
            val currentUserId = resolveCurrentUserId(userId)

            val sql = """
                SELECT 
                    t.thread_id,
                    CASE 
                        WHEN t.user1_id = ? THEN u2.username 
                        ELSE u1.username 
                    END AS contact_name,
                    last_msg.body  AS last_message,
                    last_msg.sent_at AS last_sent_at
                FROM MESSAGE_THREAD t
                JOIN "USER" u1 ON t.user1_id = u1.user_id
                JOIN "USER" u2 ON t.user2_id = u2.user_id
                LEFT JOIN MESSAGE last_msg ON last_msg.message_id = (
                    SELECT m.message_id 
                    FROM MESSAGE m
                    WHERE m.thread_id = t.thread_id
                    ORDER BY m.sent_at DESC, m.message_id DESC
                    LIMIT 1
                )
                WHERE t.user1_id = ? OR t.user2_id = ?
                ORDER BY last_sent_at DESC NULLS LAST, t.thread_id DESC;
            """.trimIndent()

            val args = arrayOf(
                currentUserId.toString(),
                currentUserId.toString(),
                currentUserId.toString()
            )

            val cursor = db.rawQuery(sql, args)
            val result = mutableListOf<MessageThreadUi>()

            cursor.use { c ->
                val idxThreadId   = c.getColumnIndexOrThrow("thread_id")
                val idxName       = c.getColumnIndexOrThrow("contact_name")
                val idxLastMsg    = c.getColumnIndexOrThrow("last_message")
                val idxLastSentAt = c.getColumnIndexOrThrow("last_sent_at")

                while (c.moveToNext()) {
                    val name = c.getString(idxName)
                    val lastMsg = c.getString(idxLastMsg) ?: "No messages yet"
                    val sentAt = c.getString(idxLastSentAt) ?: ""

                    result += MessageThreadUi(
                        id = c.getInt(idxThreadId),
                        senderName = name,
                        initials = initialsFromName(name),
                        lastMessage = lastMsg,
                        timeAgo = sentAt,   // keep it simple: show timestamp string
                        unreadCount = 0
                    )
                }
            }

            result
        }

    override suspend fun getMessagesForThread(threadId: Int): List<ChatMessageUi> =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getReadableDatabase()
            val currentUserId = resolveCurrentUserId(1)

            val cursor = db.rawQuery(
                """
                SELECT message_id, sender_id, body, sent_at
                FROM MESSAGE
                WHERE thread_id = ?
                ORDER BY sent_at ASC, message_id ASC;
                """.trimIndent(),
                arrayOf(threadId.toString())
            )

            val result = mutableListOf<ChatMessageUi>()
            cursor.use { c ->
                val idxId    = c.getColumnIndexOrThrow("message_id")
                val idxSender= c.getColumnIndexOrThrow("sender_id")
                val idxBody  = c.getColumnIndexOrThrow("body")
                val idxTime  = c.getColumnIndexOrThrow("sent_at")

                while (c.moveToNext()) {
                    val senderId = c.getInt(idxSender)
                    result += ChatMessageUi(
                        id = c.getInt(idxId),
                        isMe = (senderId == currentUserId),
                        text = c.getString(idxBody),
                        time = c.getString(idxTime)
                    )
                }
            }
            result
        }

    override suspend fun sendMessage(threadId: Int, text: String): ChatMessageUi =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getWritableDatabase()
            val currentUserId = resolveCurrentUserId(1)

            // Insert message
            val insertStmt = db.compileStatement(
                """
                INSERT INTO MESSAGE(thread_id, sender_id, body)
                VALUES (?, ?, ?);
                """.trimIndent()
            )
            insertStmt.bindLong(1, threadId.toLong())
            insertStmt.bindLong(2, currentUserId.toLong())
            insertStmt.bindString(3, text)
            val newIdLong = insertStmt.executeInsert()

            // Get timestamp (sent_at)
            val cursor = db.rawQuery(
                """
                SELECT sent_at 
                FROM MESSAGE 
                WHERE message_id = ?;
                """.trimIndent(),
                arrayOf(newIdLong.toString())
            )

            var sentAt = "Now"
            cursor.use { c ->
                if (c.moveToFirst()) {
                    sentAt = c.getString(c.getColumnIndexOrThrow("sent_at"))
                }
            }

            ChatMessageUi(
                id = newIdLong.toInt(),
                isMe = true,
                text = text,
                time = sentAt
            )
        }

    private fun initialsFromName(name: String): String {
        val parts = name.trim().split(" ")
            .filter { it.isNotBlank() }

        val chars = when {
            parts.isEmpty() -> listOf('U')
            parts.size == 1 -> listOf(parts[0].first())
            else -> listOf(parts[0].first(), parts[1].first())
        }

        return chars.joinToString("").uppercase()
    }
}
