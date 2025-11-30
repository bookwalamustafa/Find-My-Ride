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
import app.composeapp.generated.resources.ic_calendar
import app.composeapp.generated.resources.ic_clock
import app.composeapp.generated.resources.ic_two_people
import app.composeapp.generated.resources.ic_dollar_sign
import org.jetbrains.compose.resources.painterResource
import com.example.demo.feature.rides.RideInput

@Composable
fun FindRideScreen() {
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
                text = "Find a Ride",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your trip details",
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
                RideInput(label = "Pickup Location", icon = painterResource(Res.drawable.ic_location_ping), placeholder = "30th Street Station")
                RideInput(label = "Drop-off Location", icon = painterResource(Res.drawable.ic_location_ping), placeholder = "Cira Green")

                // Date & Time
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RideInput(
                        label = "Date",
                        icon = painterResource(Res.drawable.ic_calendar),
                        placeholder = "mm/dd/yyyy",
                        modifier = Modifier.weight(1f)
                    )
                    RideInput(
                        label = "Time",
                        icon = painterResource(Res.drawable.ic_clock),
                        placeholder = "--:-- --",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Seats & Price
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    RideInput(
                        label = "Seats Needed",
                        icon = painterResource(Res.drawable.ic_two_people),
                        placeholder = "2",
                        modifier = Modifier.weight(1f)
                    )
                    RideInput(
                        label = "Max Price",
                        icon = painterResource(Res.drawable.ic_dollar_sign),
                        placeholder = "20",
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
                        text = "Search for Rides",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}