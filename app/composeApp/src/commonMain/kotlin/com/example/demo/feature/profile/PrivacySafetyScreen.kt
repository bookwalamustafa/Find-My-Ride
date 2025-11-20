package com.example.demo.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySafetyScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var showProfilePublicly by remember { mutableStateOf(true) }
    var allowMessagesFromNonContacts by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy & Safety") },
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
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F7))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Profile Visibility", style = MaterialTheme.typography.titleMedium)

            PreferenceSwitchRow(
                title = "Show profile to other riders",
                subtitle = "Your name and photo will be visible in rides",
                checked = showProfilePublicly,
                onCheckedChange = { showProfilePublicly = it }
            )

            Spacer(Modifier.height(16.dp))

            Text("Messages", style = MaterialTheme.typography.titleMedium)

            PreferenceSwitchRow(
                title = "Allow messages from non-contacts",
                subtitle = "People who haven’t ridden with you can message you",
                checked = allowMessagesFromNonContacts,
                onCheckedChange = { allowMessagesFromNonContacts = it }
            )

            Spacer(Modifier.height(16.dp))

            Text("Data & Security", style = MaterialTheme.typography.titleMedium)

            OutlinedButton(
                onClick = { /* TODO: export data */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Download my data")
            }

            OutlinedButton(
                onClick = { /* TODO: report a problem */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Report a safety issue")
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { /* TODO: save privacy settings */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Privacy Settings")
            }
        }
    }
}
