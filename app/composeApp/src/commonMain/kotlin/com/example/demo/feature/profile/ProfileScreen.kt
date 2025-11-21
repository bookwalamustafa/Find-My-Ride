package com.example.demo.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.feature.profile.components.SettingsCard
import com.example.demo.feature.profile.components.VehicleEditDialog
import com.example.demo.feature.profile.components.VehiclesCard
import com.example.demo.feature.profile.pages.AccountSettingsScreen
import com.example.demo.feature.profile.pages.PreferencesScreen
import com.example.demo.feature.profile.pages.PrivacySafetyScreen
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

enum class ProfilePage {
    Overview,
    AccountSettings,
    Preferences,
    PrivacySafety
}

@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel = remember { ProfileViewModel() },
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(ProfilePage.Overview) }
    val state by viewModel.uiState.collectAsState()

    when (currentPage) {
        ProfilePage.Overview -> ProfileScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onAccountSettingsClick = { currentPage = ProfilePage.AccountSettings },
            onPreferencesClick = { currentPage = ProfilePage.Preferences },
            onPrivacySafetyClick = { currentPage = ProfilePage.PrivacySafety },
            modifier = modifier
        )

        ProfilePage.AccountSettings -> AccountSettingsScreen(
            state = state.account,
            onChange = { updated ->
                viewModel.onEvent(ProfileEvent.AccountSettingsChanged(
                    fullName = updated.fullName,
                    email = updated.email,
                    phone = updated.phone,
                    password = updated.password
                ))
            },
            onSave = { viewModel.onEvent(ProfileEvent.SaveAccountSettings) },
            onDelete = { viewModel.onEvent(ProfileEvent.DeleteAccount) },
            onBack = { currentPage = ProfilePage.Overview }
        )


        ProfilePage.Preferences -> PreferencesScreen(
            prefs = state.preferences,
            onChange = { newPrefs ->
                viewModel.onEvent(
                    ProfileEvent.PreferencesChanged(
                        notificationsEnabled = newPrefs.notificationsEnabled,
                        emailUpdatesEnabled = newPrefs.emailUpdatesEnabled,
                        darkModeEnabled = newPrefs.darkModeEnabled
                    )
                )
            },
            onSave = { viewModel.onEvent(ProfileEvent.SavePreferences) },
            onBack = { currentPage = ProfilePage.Overview },
            modifier = modifier
        )

        ProfilePage.PrivacySafety -> PrivacySafetyScreen(
            modifier = modifier,
            onBack = { currentPage = ProfilePage.Overview }
        )
    }
}


@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    onAccountSettingsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onPrivacySafetyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        ProfileHeader(state)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatsCard(state)
            VerifiedCard()
            VehiclesCard(state, onEvent)
            SettingsCard(
                onAccountSettingsClick = onAccountSettingsClick,
                onPreferencesClick = onPreferencesClick,
                onPrivacySafetyClick = onPrivacySafetyClick
            )
        }
    }

    if (state.isVehicleDialogOpen) {
        VehicleEditDialog(state, onEvent)
    }
}


@Composable
private fun ProfileHeader(state: ProfileUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DrexelBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar circle with initials
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(DrexelGold),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.name.split(" ")
                            .take(2)
                            .joinToString("") { it.first().uppercase() },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrexelBlue
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = state.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = DrexelGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${state.rating}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                        )
                        Text(
                            text = "${state.totalRides} rides",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsCard(state: ProfileUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                title = "\$${state.savedAmount}",
                subtitle = "Saved"
            )
            StatItem(
                title = "${state.passengerRides}",
                subtitle = "As Passenger"
            )
            StatItem(
                title = "${state.driverRides}",
                subtitle = "As Driver"
            )
        }
    }
}

@Composable
private fun StatItem(title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun VerifiedCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = Color(0xFF42B883),
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Verified Account", fontWeight = FontWeight.SemiBold)
                Text(
                    "ID and phone verified",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}



























