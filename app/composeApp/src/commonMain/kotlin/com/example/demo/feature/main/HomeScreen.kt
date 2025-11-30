package com.example.demo.feature.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onFindRideClick: () -> Unit = {},
    onOfferRideClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FieldBackground)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Welcome back!",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Where would you like to go?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    IconButton(onClick = { /* settings later */ }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------- MAIN CARDS ----------
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f, fill = true),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Find a Ride card
            HomeActionCard(
                title = "Find a Ride",
                subtitle = "Search for available rides to your destination",
                iconBgColor = Color(0xFFE3F2FD),
                iconTint = DrexelBlue,
                icon = Icons.Filled.People,
                buttonText = "Get started →",
                onClick = onFindRideClick
            )

            // Offer a Ride card
            HomeActionCard(
                title = "Offer a Ride",
                subtitle = "Share your trip and earn money",
                iconBgColor = Color(0xFFFFF4CC),
                iconTint = DrexelGold,
                icon = Icons.Filled.DirectionsCar,
                buttonText = "Get started →",
                onClick = onOfferRideClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Rides",
                    value = "12",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Rating",
                    value = "4.8★",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Saved",
                    value = "$142",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun HomeActionCard(
    title: String,
    subtitle: String,
    iconBgColor: Color,
    iconTint: Color,
    icon: ImageVector,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = DrexelBlue
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = HintGrey
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(iconBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint
                    )
                }

                TextButton(onClick = onClick) {
                    Text(
                        text = buttonText,
                        color = DrexelBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DrexelBlue
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = HintGrey
            )
        }
    }
}
