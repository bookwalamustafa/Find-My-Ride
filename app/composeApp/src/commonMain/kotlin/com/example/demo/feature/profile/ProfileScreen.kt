package com.example.demo.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
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
            modifier = modifier,
            onBack = { currentPage = ProfilePage.Overview }
        )

        ProfilePage.Preferences -> PreferencesScreen(
            modifier = modifier,
            onBack = { currentPage = ProfilePage.Overview }
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

@Composable
private fun VehiclesCard(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Vehicles", fontWeight = FontWeight.SemiBold)

                TextButton(
                    onClick = { onEvent(ProfileEvent.AddVehicleClicked) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "+ Add",
                        color = DrexelGold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            state.vehicles.forEach { vehicle ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            onEvent(ProfileEvent.VehicleClicked(vehicle.id))
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F6FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = DrexelBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "${vehicle.make} ${vehicle.model}",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "${vehicle.color} · ${vehicle.plate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                "${vehicle.seatsTotal} seats · ${vehicle.year}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun SettingsCard(
    onAccountSettingsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    onPrivacySafetyClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Settings", fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(12.dp))

            SettingsRow(
                icon = Icons.Default.Settings,
                label = "Account Settings",
                onClick = onAccountSettingsClick
            )
            SettingsRow(
                icon = Icons.Default.Tune,
                label = "Preferences",
                onClick = onPreferencesClick
            )
            SettingsRow(
                icon = Icons.Default.Shield,
                label = "Privacy & Safety",
                onClick = onPrivacySafetyClick
            )
        }
    }
}

@Composable
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )
    }
}

@Composable
fun VehicleEditDialog(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    val edit = state.vehicleEdit

    Dialog(onDismissRequest = { onEvent(ProfileEvent.VehicleDialogDismissed) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DrexelBlue)
                        .padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Edit Vehicle",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = edit.make,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditMakeChanged(it)) },
                        label = { Text("Make") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edit.model,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditModelChanged(it)) },
                        label = { Text("Model") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edit.color,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditColorChanged(it)) },
                        label = { Text("Color") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edit.plate,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditPlateChanged(it)) },
                        label = { Text("Plate") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edit.seatsTotal,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditSeatsChanged(it)) },
                        label = { Text("Seats (total)") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edit.year,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditYearChanged(it)) },
                        label = { Text("Year") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = edit.funFact,
                        onValueChange = { onEvent(ProfileEvent.VehicleEditFunFactChanged(it)) },
                        label = { Text("Fun fact about this car") },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onEvent(ProfileEvent.VehicleDialogDismissed) }
                    ) {
                        Text("Cancel", color = DrexelBlue)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { onEvent(ProfileEvent.SaveVehicleChanges) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DrexelGold,
                            contentColor = DrexelBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsDialog(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    val option = state.selectedSettingsOption ?: return

    val title = when (option) {
        SettingsOption.AccountSetting -> "Account Settings"
        SettingsOption.Preferences -> "Preferences"
        SettingsOption.PrivacySetting -> "Privacy & Safety"
    }

    Dialog(onDismissRequest = { onEvent(ProfileEvent.SettingsDialogDismissed) }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) {
            Column (modifier = Modifier.fillMaxWidth()) {

                // header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DrexelBlue)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "This is where $title options will go.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "You can later replace this pop up with a full screen.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onEvent(ProfileEvent.SettingsDialogDismissed) }
                    ) {
                        Text("Close", color = DrexelBlue)
                    }
                }
            }
        }
    }
}






























