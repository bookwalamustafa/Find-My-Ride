package com.example.demo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.composeapp.generated.resources.Res
import app.composeapp.generated.resources.ic_back_arrow
import app.composeapp.generated.resources.ic_location_ping
import app.composeapp.generated.resources.ic_clock
import app.composeapp.generated.resources.ic_two_people
import app.composeapp.generated.resources.ic_dollar_sign
import app.composeapp.generated.resources.ic_vehicle
import org.jetbrains.compose.resources.painterResource
import com.example.demo.feature.rides.RideInput

@Composable
fun OfferRideScreen() {
    val bgColor = Color(0xFFF3F4F6)
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // 1. Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(DrexelBlue)
                .padding(24.dp)
        ) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Offer a Ride",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Share your journey details",
                style = MaterialTheme.typography.titleLarge,
                color = HintGrey
            )
        }

        // 2. The Floating Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 175.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .verticalScroll(scrollState), // Make form scrollable on small screens
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Locations
                RideInput(label = "Origin Location", icon = painterResource(Res.drawable.ic_location_ping), placeholder = "University Crossings")
                RideInput(label = "Destination", icon = painterResource(Res.drawable.ic_location_ping), placeholder = "Korman Center")

                // Departure Time
                RideInput(
                    label = "Departure Time",
                    icon = painterResource(Res.drawable.ic_clock),
                    placeholder = "mm/dd/yyyy --:-- --",
                )

                // Vehicle Selection
                RideInput(
                    label = "Choose Vehicle",
                    icon = painterResource(Res.drawable.ic_vehicle),
                    placeholder = "Tesla Model Y (Blue)",
                )

                // Available Seats
                RideInput(
                    label = "Available Seats",
                    icon = painterResource(Res.drawable.ic_two_people),
                    placeholder = "2",
                )

                // Base Price & Price Per Mile
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RideInput(
                        label = "Base Price",
                        icon = painterResource(Res.drawable.ic_dollar_sign),
                        placeholder = "5.00",
                        modifier = Modifier.weight(1f)
                    )
                    RideInput(
                        label = "Per Mile",
                        icon = painterResource(Res.drawable.ic_dollar_sign),
                        placeholder = "0.50",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Search Button
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DrexelGold,
                        contentColor = DrexelBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Publish Offer",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}