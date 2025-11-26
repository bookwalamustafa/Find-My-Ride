package com.example.demo.feature.rides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.composeapp.generated.resources.Res
import app.composeapp.generated.resources.ic_back_arrow
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.HintGrey
import com.example.demo.ui.theme.FieldBackground
import org.jetbrains.compose.resources.painterResource

@Composable
fun AvailableRidesScreen() {
    // Dummy Data
    val bestMatches = listOf(
        RideOption("Abdul B.", 4.92, 11.71, 3, "Tesla Model Y (Blue)", "30th Street Station", "Cira Green", "Today 5:30 PM", true),
        RideOption("Sarah M.", 4.87, 12.50, 2, "Honda Accord (Silver)", "30th Street Station", "Cira Green", "Today 5:45 PM", true),
        RideOption("James K.", 4.85, 10.99, 4, "Toyota Camry (Black)", "30th Street Station", "Cira Green", "Today 6:00 PM", true)
    )

    val otherMatches = listOf(
        RideOption("Lisa P.", 4.73, 14.25, 2, "Mazda CX-5 (Red)", "30th Street Station", "Cira Green", "Today 5:15 PM", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FieldBackground)
    ) {
        // Custom Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DrexelBlue)
                .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 32.dp)
        ) {
            // Back Arrow and Filter Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Handle Back */ }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_back_arrow),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Button(
                    onClick = { /* Handle Filter */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4B6E)), // Slightly lighter blue
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(painterResource(Res.drawable.ic_back_arrow), contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Filter")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Available Rides",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "7 rides found for your route",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        // Scrollable List
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Best Matches
            item {
                SectionBadge(text = "Best Matches", color = DrexelGold, textColor = DrexelBlue)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(bestMatches) { ride ->
                RideCard(ride)
            }

            // Other Matches
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionBadge(text = "Other Matches", color = Color.White, textColor = HintGrey, isBordered = true)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(otherMatches) { ride ->
                RideCard(ride)
            }
        }
    }
}

@Composable
fun RideCard(ride: RideOption) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Name, Star, Price, and Seats
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = ride.driverName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DrexelBlue
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(Res.drawable.ic_back_arrow),
                        contentDescription = "Rating",
                        tint = DrexelGold,
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = ride.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HintGrey,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${ride.price}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DrexelBlue
                    )
                    Text(
                        text = "${ride.seats} seats",
                        style = MaterialTheme.typography.labelSmall,
                        color = HintGrey
                    )
                }
            }

            // Car Model
            Text(
                text = ride.carModel,
                style = MaterialTheme.typography.bodyMedium,
                color = HintGrey
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Route
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = ride.pickup,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HintGrey
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(Res.drawable.ic_back_arrow),
                    contentDescription = "to",
                    tint = HintGrey,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = ride.dropoff,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HintGrey
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time
            Text(
                text = ride.time,
                style = MaterialTheme.typography.bodyMedium,
                color = DrexelBlue
            )
        }
    }
}

@Composable
fun SectionBadge(
    text: String,
    color: Color,
    textColor: Color,
    isBordered: Boolean = false
) {
    Surface(
        color = color,
        shape = RoundedCornerShape(50),
        border = if (isBordered) null else null,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}