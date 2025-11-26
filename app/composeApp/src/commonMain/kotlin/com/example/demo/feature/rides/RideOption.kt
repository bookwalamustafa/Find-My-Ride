package com.example.demo.feature.rides

// Simple data class to hold ride info
data class RideOption(
    val driverName: String,
    val rating: Double,
    val price: Double,
    val seats: Int,
    val carModel: String,
    val pickup: String,
    val dropoff: String,
    val time: String,
    val isBestMatch: Boolean = false
)