package com.example.demo.feature.profile

data class VehicleUi(
    val id: Int,
    val ownerUserId: Int,
    val make: String,
    val model: String,
    val color: String,
    val plate: String,
    val seatsTotal: Int,
    val year: Int,
    val funFact: String
)

// Used only while editing in the dialog
data class VehicleEditState(
    val id: Int? = null,
    val ownerUserId: Int? = null,
    val make: String = "",
    val model: String = "",
    val color: String = "",
    val plate: String = "",
    val seatsTotal: String = "",   // keep as String for text field
    val year: String = "",
    val funFact: String = ""
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
            ownerUserId = 42, // placeholder
            make = "Tesla",
            model = "Model Y",
            color = "Blue",
            plate = "ABC-1234",
            seatsTotal = 5,
            year = 2022,
            funFact = "First EV on campus"
        )
    ),
    val isSettingsDialogOpen: Boolean = false,
    val selectedSettingsOption: SettingsOption? = null,

    // dialog / editing state
    val isVehicleDialogOpen: Boolean = false,
    val vehicleEdit: VehicleEditState = VehicleEditState(),
    val preferences: PreferencesState = PreferencesState(),
)


data class PreferencesState(
    val notificationEnabled: Boolean = true,
    val emailUpdatesEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
)
