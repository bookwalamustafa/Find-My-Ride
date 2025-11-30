package com.example.demo.feature.rides

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.FieldBackground
import com.example.demo.ui.theme.HintGrey

private enum class MyRidesTab { Upcoming, Completed }

data class RideHistoryItem(
    val id: Int,
    val status: String,
    val role: String,
    val driverName: String,
    val pickup: String,
    val dropoff: String,
    val date: String,
    val time: String,
    val price: String
)

@Composable
fun MyRidesScreen(
    modifier: Modifier = Modifier
) {
    // fake data for now
    val upcomingRides = listOf(
        RideHistoryItem(
            id = 1,
            status = "Confirmed",
            role = "Passenger",
            driverName = "Abdul B.",
            pickup = "30th Street Station",
            dropoff = "Cira Green",
            date = "Nov 11, 2025",
            time = "5:30 PM",
            price = "$11.71"
        )
    )

    val completedRides = listOf(
            RideHistoryItem(
                id = 2,
                status = "Passenger",     // only pill we show
                role = "",                // leave empty so no second pill
                driverName = "Sarah M.",
                pickup = "University Crossings",
                dropoff = "Downtown Philadelphia",
                date = "Nov 5, 2025",
                time = "8:00 AM",
                price = "$15.5"
            ),
    RideHistoryItem(
        id = 3,
        status = "Driver",
        role = "",
        driverName = "You",       // or whoever
        pickup = "West Philadelphia",
        dropoff = "King of Prussia",
        date = "Nov 1, 2025",
        time = "6:30 PM",
        price = "$28"
    ),
    RideHistoryItem(
        id = 4,
        status = "Passenger",
        role = "",
        driverName = "Michael T.",
        pickup = "Temple University",
        dropoff = "Chestnut Hill",
        date = "Oct 28, 2025",
        time = "3:15 PM",
        price = "$12.75"
    )
    )

    var currentTab by remember { mutableStateOf(MyRidesTab.Upcoming) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FieldBackground)
    ) {
        // ------- HEADER --------
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
                Text(
                    text = "My Rides",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your ride history & upcoming trips",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // segmented control
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SegmentedTab(
                            text = "Upcoming",
                            selected = currentTab == MyRidesTab.Upcoming,
                            modifier = Modifier.weight(1f)
                        ) { currentTab = MyRidesTab.Upcoming }

                        SegmentedTab(
                            text = "Completed",
                            selected = currentTab == MyRidesTab.Completed,
                            modifier = Modifier.weight(1f)
                        ) { currentTab = MyRidesTab.Completed }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ------- LIST CONTENT -------
        val ridesToShow =
            if (currentTab == MyRidesTab.Upcoming) upcomingRides else completedRides

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ridesToShow, key = { it.id }) { ride ->
                RideHistoryCard(ride)
            }

            if (ridesToShow.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No rides in this section yet.",
                            color = HintGrey
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SegmentedTab(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bg = if (selected) Color.White else Color.Transparent
    val textColor = if (selected) DrexelBlue else HintGrey

    Box(
        modifier = modifier
            .fillMaxHeight()
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(0.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = if (selected) Color(0xFFEFF3FF) else Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun RideHistoryCard(ride: RideHistoryItem) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // always show the first pill
                    PillChip(
                        text = ride.status,
                        bg = Color(0xFFE3F8EA),
                        textColor = Color(0xFF2C7A43)
                    )

                    // only show second pill when role is non-blank
                    if (ride.role.isNotBlank()) {
                        PillChip(
                            text = ride.role,
                            bg = Color(0xFFEAF0FF),
                            textColor = DrexelBlue
                        )
                    }
                }
                Text(
                    text = ride.price,
                    fontWeight = FontWeight.Bold,
                    color = DrexelBlue
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Driver: ${ride.driverName}",
                style = MaterialTheme.typography.bodyMedium,
                color = HintGrey
            )

            Spacer(modifier = Modifier.height(16.dp))

            // locations
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = HintGrey,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ride.pickup,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HintGrey
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = HintGrey,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ride.dropoff,
                    style = MaterialTheme.typography.bodyMedium,
                    color = HintGrey
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // date + time
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = HintGrey,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${ride.date}   ${ride.time}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HintGrey
                )
            }
        }
    }
}

@Composable
private fun PillChip(
    text: String,
    bg: Color,
    textColor: Color
) {
    Surface(
        color = bg,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
