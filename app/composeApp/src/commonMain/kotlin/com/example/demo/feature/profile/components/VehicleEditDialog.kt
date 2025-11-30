package com.example.demo.feature.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.demo.feature.profile.ProfileEvent
import com.example.demo.feature.profile.ProfileUiState
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

@Composable
fun VehicleEditDialog(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    val edit = state.vehicleEdit
    val isEditing = edit.id != null

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
                        text = if (isEditing) "Edit Vehicle" else "Add Vehicle",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // Form fields
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

                // Buttons (Delete + Cancel + Save)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Only show Delete when editing an existing car
                    if (isEditing) {
                        OutlinedButton(
                            onClick = { onEvent(ProfileEvent.DeleteVehicleClicked) },
                            border = BorderStroke(1.dp, Color.Red),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            )
                        ) {
                            Text("Delete")
                        }
                    }

                    Spacer(Modifier.weight(1f))

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
