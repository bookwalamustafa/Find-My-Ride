package com.example.demo.feature.profile

data class VehicleUi(
    val name: String,
    val colorAndPlate: String
)

data class ProfileUiState(
    val name: String = "John Doe",
    val email: String = "john.doe@email.com",
    val rating: Double = 4.8,
    val totalRides: Int = 12,
    val savedAmount: Int = 142,
    val passengerRides: Int = 8,
    val driverRides: Int = 4,
    val isVerified: Boolean = true,
    val vehicles: List<VehicleUi> = listOf(
        VehicleUi(
            name = "Tesla Model Y",
            colorAndPlate = "Blue · ABC-1234"
        )
    )
)
