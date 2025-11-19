package com.example.demo.feature.profile

data class VehicleUi(
    val id: Int,
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
            id = 1,
            name = "Tesla Model Y",
            colorAndPlate = "Blue · ABC-1234"
        )
    ),

    // dialog editing state
    val isVehicleDialogOpen: Boolean = false,
    val editingVehicleId: Int? = null,
    val editingVehicleName: String = "",
    val editingVehicleDetails: String = "",
)
