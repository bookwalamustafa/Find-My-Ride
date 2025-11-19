package com.example.demo.feature.profile

sealed interface ProfileEvent {
    data class  VehicleClicked(val vehicleId: Int) : ProfileEvent
    data object VehicleDialogDismissed : ProfileEvent
    data class EditVehicleNameChanged(val value: String) : ProfileEvent
    data class EditVehicleDetailsChanged(val value: String) : ProfileEvent
    data object SaveVehicleChanges : ProfileEvent
}