package com.example.demo.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.feature.profile.ProfileRoute
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

enum class MainTab { Home, Rides, Messages, Profile }

@Composable
fun MainRoute() {
    var currentTab by remember { mutableStateOf(MainTab.Profile) } // start on profile for now

    Scaffold(
        bottomBar = {
            MainBottomNav(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        containerColor = Color(0xFFF5F5F7)
    ) { padding ->
        when (currentTab) {
            MainTab.Home -> {
                // TODO: replace with real HomeRoute
                Text(
                    text = "Home screen placeholder",
                    modifier = Modifier.padding(padding)
                )
            }
            MainTab.Rides -> {
                Text(
                    text = "My Rides placeholder",
                    modifier = Modifier.padding(padding)
                )
            }
            MainTab.Messages -> {
                Text(
                    text = "Messages placeholder",
                    modifier = Modifier.padding(padding)
                )
            }
            MainTab.Profile -> {
                ProfileRoute(modifier = Modifier.padding(padding))
            }
        }
    }
}

@Composable
private fun MainBottomNav(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        NavigationBarItem(
            selected = currentTab == MainTab.Home,
            onClick = { onTabSelected(MainTab.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentTab == MainTab.Rides,
            onClick = { onTabSelected(MainTab.Rides) },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "My Rides") },
            label = { Text("My Rides") }
        )
        NavigationBarItem(
            selected = currentTab == MainTab.Messages,
            onClick = { onTabSelected(MainTab.Messages) },
            icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Messages") },
            label = { Text("Messages") }
        )
        NavigationBarItem(
            selected = currentTab == MainTab.Profile,
            onClick = { onTabSelected(MainTab.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
