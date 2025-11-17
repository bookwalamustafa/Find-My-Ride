package com.example.demo.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.composeapp.generated.resources.Res
import app.composeapp.generated.resources.ic_back_arrow

import org.jetbrains.compose.resources.painterResource

@Composable
fun FindRideScreen(
    onBackClick: () -> Unit,
) {
    val bgColor = Color(0xFFF3F4F6)

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
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Res.drawable.ic_back_arrow),
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Find a Ride",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your trip details",
                style = MaterialTheme.typography.titleLarge,
                color = HintGrey,
            )
        }
    }
}