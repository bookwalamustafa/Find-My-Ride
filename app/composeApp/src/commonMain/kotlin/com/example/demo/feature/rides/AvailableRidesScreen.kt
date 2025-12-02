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
import androidx.compose.ui.unit.sp
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold
import com.example.demo.ui.theme.HintGrey
import com.example.demo.ui.theme.FieldBackground
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon

import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import com.example.demo.feature.db.RideOffer
import com.example.demo.feature.db.RideRepository

@Composable
fun AvailableRidesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    rideRepository: RideRepository
) {
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var rideOffers by remember { mutableStateOf<List<RideOffer>>(emptyList()) }

    // load from DB on first composition
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            errorMessage = null
            rideOffers = rideRepository.getOpenRideOffers()
        } catch (e: Exception) {
            errorMessage = e.message ?: "Failed to load rides"
        } finally {
            isLoading = false
        }
    }

    // simple “best / other” split (first 3 vs rest)
    val bestMatches: List<RideOffer> =
        if (rideOffers.size > 3) rideOffers.take(3) else rideOffers
    val otherMatches: List<RideOffer> =
        if (rideOffers.size > 3) rideOffers.drop(3) else emptyList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FieldBackground)
    ) {
        // your existing header / back button code stays the same

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = DrexelBlue)
            }
            return@Column
        }

        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red
                )
            }
            return@Column
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (bestMatches.isNotEmpty()) {
                item {
                    SectionBadge(
                        text = "Best Matches",
                        color = DrexelGold,
                        textColor = DrexelBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(bestMatches) { offer ->
                    RideCard(offer = offer)
                }
            }

            if (otherMatches.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionBadge(
                        text = "Other Matches",
                        color = Color.White,
                        textColor = HintGrey,
                        isBordered = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(otherMatches) { offer ->
                    RideCard(offer = offer)
                }
            }
        }
    }
}



@Composable
fun RideCard(offer: RideOffer) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // You don't currently join driver name / rating / car model in RideOffer,
            // so we’ll show what we *do* have and use placeholders where needed.

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Driver #${offer.driverId}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DrexelBlue
                    )
                    Text(
                        text = "Vehicle #${offer.vehicleId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = HintGrey
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${"%.2f".format(offer.priceBase)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DrexelBlue
                    )
                    Text(
                        text = "${offer.seatsAvailable} seats",
                        style = MaterialTheme.typography.bodySmall,
                        color = HintGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${offer.fromName} → ${offer.toName}",
                style = MaterialTheme.typography.bodyMedium,
                color = DrexelBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = offer.departAt,
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
