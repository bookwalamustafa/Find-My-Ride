package com.example.demo.feature.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.VehicleClicked -> {
                val vehicle = _uiState.value.vehicles.firstOrNull { it.id == event.vehicleId }
                    ?: return
                _uiState.value = _uiState.value.copy(
                    isVehicleDialogOpen = true,
                    editingVehicleId = vehicle.id,
                    editingVehicleName = vehicle.name,
                    editingVehicleDetails = vehicle.colorAndPlate
                )
            }

            ProfileEvent.VehicleDialogDismissed -> {
                _uiState.value = _uiState.value.copy(
                    isVehicleDialogOpen = false,
                    editingVehicleId = null,
                    editingVehicleName = "",
                    editingVehicleDetails = ""
                )
            }

            is ProfileEvent.EditVehicleNameChanged -> {
                _uiState.value = _uiState.value.copy(
                    editingVehicleName = event.value
                )
            }

            is ProfileEvent.EditVehicleDetailsChanged -> {
                _uiState.value = _uiState.value.copy(
                    editingVehicleDetails = event.value
                )
            }

            ProfileEvent.SaveVehicleChanges -> {
                val state = _uiState.value
                val id = state.editingVehicleId ?: return

                val updatedVehicles = state.vehicles.map { v ->
                    if (v.id == id) {
                        v.copy(
                            name = state.editingVehicleName,
                            colorAndPlate = state.editingVehicleDetails
                        )
                    } else v
                }

                _uiState.value = state.copy(
                    vehicles = updatedVehicles,
                    isVehicleDialogOpen = false,
                    editingVehicleId = null,
                    editingVehicleName = "",
                    editingVehicleDetails = ""
                )
            }
        }
    }
}
