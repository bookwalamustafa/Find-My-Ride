package com.example.demo.feature.profile.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.profile.PreferencesState
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    prefs: PreferencesState,
    onChange: (PreferencesState) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preferences") },
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
                "Notifications",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            PreferenceSwitchRow(
                title = "Push notifications",
                subtitle = "Get alerts about upcoming rides and messages",
                checked = prefs.notificationsEnabled,
                onCheckedChange = {
                    onChange(
                        prefs.copy(notificationsEnabled = it)
                    )
                }
            )

            PreferenceSwitchRow(
                title = "Email updates",
                subtitle = "Receive trip summaries and announcements",
                checked = prefs.emailUpdatesEnabled,
                onCheckedChange = {
                    onChange(
                        prefs.copy(emailUpdatesEnabled = it)
                    )
                }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Appearance",
                style = MaterialTheme.typography.titleMedium,
                color = DrexelBlue
            )

            PreferenceSwitchRow(
                title = "Dark mode",
                subtitle = "Use a darker color scheme at night",
                checked = prefs.darkModeEnabled,
                onCheckedChange = {
                    onChange(
                        prefs.copy(darkModeEnabled = it)
                    )
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
                Text("Save Preferences")
            }
        }
    }
}

@Composable
fun PreferenceSwitchRow(
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
