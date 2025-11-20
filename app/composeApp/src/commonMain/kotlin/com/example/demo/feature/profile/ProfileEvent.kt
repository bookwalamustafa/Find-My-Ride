package com.example.demo.feature.profile

sealed interface ProfileEvent {
    data class VehicleClicked(val vehicleId: Int) : ProfileEvent
    data object VehicleDialogDismissed : ProfileEvent

    data class VehicleEditMakeChanged(val value: String) : ProfileEvent
    data class VehicleEditModelChanged(val value: String) : ProfileEvent
    data class VehicleEditColorChanged(val value: String) : ProfileEvent
    data class VehicleEditPlateChanged(val value: String) : ProfileEvent
    data class VehicleEditSeatsChanged(val value: String) : ProfileEvent
    data class VehicleEditYearChanged(val value: String) : ProfileEvent
    data class VehicleEditFunFactChanged(val value: String) : ProfileEvent

    data object SaveVehicleChanges : ProfileEvent
}
