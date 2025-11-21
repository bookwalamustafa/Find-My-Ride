package com.example.demo.feature.profile.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.profile.PrivacySafetyState
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacySafetyScreen(
    state: PrivacySafetyState,
    onChange: (PrivacySafetyState) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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

            Text(
                "Profile Visibility",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            PrivacySwitchRow(
                title = "Show profile publicly",
                subtitle = "Allow other students to view your basic profile",
                checked = state.showProfilePublicly,
                onCheckedChange = {
                    onChange(state.copy(showProfilePublicly = it))
                }
            )

            PrivacySwitchRow(
                title = "Allow messages from non-contacts",
                subtitle = "Let people who haven't ridden with you send requests",
                checked = state.allowMessagesFromNonContacts,
                onCheckedChange = {
                    onChange(state.copy(allowMessagesFromNonContacts = it))
                }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Sharing",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            PrivacySwitchRow(
                title = "Share trip history with friends",
                subtitle = "Friends can see your past rides and eco impact",
                checked = state.shareTripHistoryWithFriends,
                onCheckedChange = {
                    onChange(state.copy(shareTripHistoryWithFriends = it))
                }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                "Security",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            PrivacySwitchRow(
                title = "Two-factor authentication",
                subtitle = "Add an extra step when logging into your account",
                checked = state.twoFactorEnabled,
                onCheckedChange = {
                    onChange(state.copy(twoFactorEnabled = it))
                }
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrexelGold,
                    contentColor = DrexelBlue
                )
            ) {
                Text("Save Privacy Settings")
            }
        }
    }
}

@Composable
private fun PrivacySwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val switchColors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = DrexelBlue,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = Color.LightGray,
        checkedBorderColor = DrexelBlue,
        uncheckedBorderColor = Color.LightGray
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = switchColors
        )
    }
}
