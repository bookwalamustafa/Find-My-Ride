package com.example.demo.ui.theme

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.composeapp.generated.resources.Res
import app.composeapp.generated.resources.ic_back_arrow

import org.jetbrains.compose.resources.painterResource

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
                .height(200.dp) // Covers top portion
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

            }
        }
    }
}

@Composable
fun RideInput(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}