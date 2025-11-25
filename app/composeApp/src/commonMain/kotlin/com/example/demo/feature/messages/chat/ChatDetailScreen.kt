package com.example.demo.feature.messages.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

data class ChatMessageUi(
    val id: Int,
    val isMe: Boolean,
    val text: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    contactName: String,
    initials: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 🔹 Local conversation state
    val messages = remember {
        mutableStateListOf(
            ChatMessageUi(1, isMe = false, text = "Hey, thanks for the ride!", time = "3h ago"),
            ChatMessageUi(2, isMe = true,  text = "Of course! Happy to help.", time = "3h ago"),
            ChatMessageUi(3, isMe = false, text = "Are we still on for 5:30 PM?", time = "2m ago"),
        )
    }

    // 🔹 Text field state
    var newMessage by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DrexelBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(contactName)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DrexelBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F7))
        ) {
            // 🔹 Messages list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(message = msg)
                    Spacer(Modifier.height(6.dp))
                }
            }

            // 🔹 Input bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...") },
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DrexelBlue,
                        unfocusedBorderColor = Color(0xFFE5E5EA),
                        cursorColor = DrexelBlue
                    ),
                    shape = RoundedCornerShape(20.dp)
                )

                Spacer(Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (newMessage.isNotBlank()) {
                            messages.add(
                                ChatMessageUi(
                                    id = (messages.maxOfOrNull { it.id } ?: 0) + 1,
                                    isMe = true,
                                    text = newMessage.trim(),
                                    time = "Now"
                                )
                            )
                            newMessage = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = DrexelGold
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatMessageUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 260.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (message.isMe) 18.dp else 4.dp,
                            bottomEnd = if (message.isMe) 4.dp else 18.dp
                        )
                    )
                    .background(if (message.isMe) DrexelBlue else Color.White)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (message.isMe) Color.White else Color(0xFF1C1C1E),
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = message.time,
                fontSize = 11.sp,
                color = Color(0xFF8E8E93)
            )
        }
    }
}
