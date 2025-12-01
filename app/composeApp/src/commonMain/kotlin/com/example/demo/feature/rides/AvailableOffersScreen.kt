package com.example.demo.feature.rides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey

@Composable
fun AvailableOfferScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPublish: () -> Unit = {}
) {
    var origin by remember { mutableStateOf("University Crossings") }
    var destination by remember { mutableStateOf("Korman Center") }
    var departure by remember { mutableStateOf("") }

    var selectedVehicle by remember { mutableStateOf("Tesla Model Y (Blue)") }
    val vehicleOptions = listOf(
        "Tesla Model Y (Blue)",
        "Honda Civic (Black)",
        "Toyota Camry (Silver)"
    )

    var selectedSeats by remember { mutableStateOf("2 Seats") }
    val seatOptions = listOf("1 Seat", "2 Seats", "3 Seats", "4 Seats")

    var basePrice by remember { mutableStateOf("5.00") }
    var perMilePrice by remember { mutableStateOf("0.50") }

    var scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FieldBackground)
            .verticalScroll(scrollState)
    ) {
        // ---------- HEADER ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(DrexelBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Offer a Ride",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Share your journey details",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- MAIN CARD ----------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Origin
                LabeledIconField(
                    label = "Origin Location",
                    value = origin,
                    onValueChange = { origin = it },
                    icon = Icons.Default.Place,
                    placeholder = "University Crossings"
                )

                // Destination
                LabeledIconField(
                    label = "Destination",
                    value = destination,
                    onValueChange = { destination = it },
                    icon = Icons.Default.Place,
                    placeholder = "Korman Center"
                )

                // Departure Time
                LabeledIconField(
                    label = "Departure Time",
                    value = departure,
                    onValueChange = { departure = it },
                    icon = Icons.Default.AccessTime,
                    placeholder = "mm/dd/yyyy  --:--  --"
                )

                // Vehicle dropdown
                LabeledDropdownField(
                    label = "Choose Vehicle",
                    value = selectedVehicle,
                    onValueChange = { selectedVehicle = it },
                    options = vehicleOptions,
                    leadingIcon = Icons.Default.DirectionsCar
                )

                // Seats dropdown
                LabeledDropdownField(
                    label = "Available Seats",
                    value = selectedSeats,
                    onValueChange = { selectedSeats = it },
                    options = seatOptions,
                    leadingIcon = Icons.Default.People
                )

                // Prices row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LabeledPriceField(
                        label = "Base Price",
                        value = basePrice,
                        onValueChange = { basePrice = it },
                        modifier = Modifier.weight(1f)
                    )

                    LabeledPriceField(
                        label = "Per Mile",
                        value = perMilePrice,
                        onValueChange = { perMilePrice = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Publish button
                Button(
                    onClick = onPublish,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DrexelGold,
                        contentColor = DrexelBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Publish Offer",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

/* ---------- Reusable components used on the screen ---------- */

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = DrexelBlue,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun LabeledIconField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    placeholder: String
) {
    Column {
        FieldLabel(label)

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = HintGrey
                )
            },
            placeholder = { Text(text = placeholder, color = HintGrey) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F6F8),
                unfocusedContainerColor = Color(0xFFF5F6F8),
                disabledContainerColor = Color(0xFFF5F6F8),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = DrexelBlue,
                unfocusedTextColor = DrexelBlue
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledDropdownField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        FieldLabel(label)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = HintGrey
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F6F8),
                    unfocusedContainerColor = Color(0xFFF5F6F8),
                    disabledContainerColor = Color(0xFFF5F6F8),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = DrexelBlue,
                    unfocusedTextColor = DrexelBlue
                ),
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LabeledPriceField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FieldLabel(label)

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Text(
                    text = "$",
                    color = HintGrey,
                    fontWeight = FontWeight.SemiBold
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F6F8),
                unfocusedContainerColor = Color(0xFFF5F6F8),
                disabledContainerColor = Color(0xFFF5F6F8),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = DrexelBlue,
                unfocusedTextColor = DrexelBlue
            ),
            singleLine = true
        )
    }
}
