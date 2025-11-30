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
import com.example.demo.feature.db.RideRepository
import com.example.demo.feature.messages.MessagesRoute
import com.example.demo.feature.messages.data.MessagesRepository
import com.example.demo.feature.profile.data.ProfileRepository
import com.example.demo.feature.rides.MyRidesScreen
import com.example.demo.ui.theme.DrexelBlue
import com.example.demo.ui.theme.DrexelGold

enum class MainTab { Home, Rides, Messages, Profile }

@Composable
fun MainRoute(
    rideRepository: RideRepository,
    profileRepository: ProfileRepository,
    messagesRepository: MessagesRepository,
) {
    var currentTab by remember { mutableStateOf(MainTab.Home) } // start on profile for now

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
                HomeScreen(
                    modifier = Modifier.padding(padding),
                    onFindRideClick = { currentTab = MainTab.Rides },
                    onOfferRideClick = { currentTab = MainTab.Rides }
                )
            }
            MainTab.Rides -> {
                MyRidesScreen(
                    modifier = Modifier.padding(padding)
                )
            }
            MainTab.Messages -> {
                MessagesRoute(
                    modifier = Modifier.padding(padding),
                    repository = messagesRepository,
                )

            }
            MainTab.Profile -> {
                ProfileRoute(
                    modifier = Modifier.padding(padding),
                    repository = profileRepository
                )
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
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = DrexelBlue,
            selectedTextColor = DrexelBlue,
            unselectedIconColor = Color.Gray,
            unselectedTextColor = Color.Gray,
            indicatorColor = DrexelGold.copy(alpha = 0.18f) // subtle gold pill behind selected
        )

        NavigationBarItem(
            selected = currentTab == MainTab.Home,
            onClick = { onTabSelected(MainTab.Home) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentTab == MainTab.Rides,
            onClick = { onTabSelected(MainTab.Rides) },
            icon = { Icon(Icons.Default.CalendarToday, contentDescription = "My Rides") },
            label = { Text("My Rides") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentTab == MainTab.Messages,
            onClick = { onTabSelected(MainTab.Messages) },
            icon = { Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Messages") },
            label = { Text("Messages") },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentTab == MainTab.Profile,
            onClick = { onTabSelected(MainTab.Profile) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = itemColors
        )
    }
}

