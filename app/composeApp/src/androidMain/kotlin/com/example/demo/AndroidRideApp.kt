package com.example.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AndroidRideApp() {
    val context = LocalContext.current
    val dbHelper = remember { RideShareDbHelper(context) }
    val repo = remember { RideRepository(dbHelper) }

    var pickup by remember { mutableStateOf("") }
    var dropoff by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var rides by remember { mutableStateOf(emptyList<Ride>()) }

    // Load rides on first launch
    LaunchedEffect(Unit) {
        rides = repo.getAllRides()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Ride Entry (Android + SQLite)",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = pickup,
                onValueChange = { pickup = it },
                label = { Text("Pickup Location") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dropoff,
                onValueChange = { dropoff = it },
                label = { Text("Dropoff Location") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (e.g., 3:30 PM)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (pickup.isNotBlank() && dropoff.isNotBlank() && time.isNotBlank()) {
                        repo.insertRide(pickup, dropoff, time)
                        rides = repo.getAllRides()
                        pickup = ""
                        dropoff = ""
                        time = ""
                    }
                }
            ) {
                Text("Save Ride")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Saved Rides:",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(rides) { ride ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text("${ride.pickup} → ${ride.dropoff}")
                        Text("Time: ${ride.time}", style = MaterialTheme.typography.bodySmall)
                        Text("ID: ${ride.id}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
