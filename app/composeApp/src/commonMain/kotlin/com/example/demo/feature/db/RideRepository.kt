package com.example.demo.feature.db

data class RideOffer(
    val offerId: Long,
    val driverId: Long,
    val vehicleId: Long,
    val fromName: String,
    val toName: String,
    val departAt: String,
    val seatsAvailable: Int,
    val priceBase: Double
)

interface RideRepository {
    suspend fun getOpenRideOffers(): List<RideOffer>
    suspend fun createRideRequest(
        riderId: Long,
        pickupLocationId: Long,
        dropoffLocationId: Long,
        earliestPickup: String,
        latestPickup: String?,
        seatsNeeded: Int
    )
}
