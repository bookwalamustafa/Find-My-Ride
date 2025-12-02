package com.example.demo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.feature.rides.RideInput

@Composable
fun OfferRideScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onPublish: () -> Unit = {}
) {
    val bgColor = Color(0xFFF3F4F6)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // ---------- HEADER (smaller) ----------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DrexelBlue)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Offer a Ride",
                style = MaterialTheme.typography.titleMedium, // smaller
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Share your journey details",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---------- CARD ----------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // tighter spacing
            ) {
                // Origin & Destination
                RideInput(
                    label = "Origin",
                    icon = Icons.Filled.LocationOn,
                    placeholder = "University Crossings"
                )
                RideInput(
                    label = "Destination",
                    icon = Icons.Filled.LocationOn,
                    placeholder = "Korman Center"
                )

                // Time + Seats (2 per row to save height)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RideInput(
                        label = "Time",
                        icon = Icons.Filled.AccessTime,
                        placeholder = "mm/dd hh:mm",
                        modifier = Modifier.weight(1f)
                    )
                    RideInput(
                        label = "Seats",
                        icon = Icons.Filled.Group,
                        placeholder = "2",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Vehicle + Price (2 per row)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RideInput(
                        label = "Vehicle",
                        icon = Icons.Filled.DirectionsCar,
                        placeholder = "Model Y",
                        modifier = Modifier.weight(1f)
                    )
                    RideInput(
                        label = "Price",
                        icon = Icons.Filled.AttachMoney,
                        placeholder = "5.00",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // ---------- BUTTON (always visible) ----------
                Button(
                    onClick = onPublish,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp), // a bit shorter
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DrexelGold,
                        contentColor = DrexelBlue
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Publish Offer",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp   // smaller
                    )
                }
            }
        }

        // tiny spacer so it doesn’t touch bottom nav
        Spacer(modifier = Modifier.height(8.dp))
    }
}
