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
                    vehicleEdit = VehicleEditState(
                        id = vehicle.id,
                        ownerUserId = vehicle.ownerUserId,
                        make = vehicle.make,
                        model = vehicle.model,
                        color = vehicle.color,
                        plate = vehicle.plate,
                        seatsTotal = vehicle.seatsTotal.toString(),
                        year = vehicle.year.toString(),
                        funFact = vehicle.funFact
                    )
                )
            }

            ProfileEvent.VehicleDialogDismissed -> {
                _uiState.value = _uiState.value.copy(
                    isVehicleDialogOpen = false,
                    vehicleEdit = VehicleEditState()
                )
            }

            is ProfileEvent.VehicleEditMakeChanged ->
                updateEdit { it.copy(make = event.value) }

            is ProfileEvent.VehicleEditModelChanged ->
                updateEdit { it.copy(model = event.value) }

            is ProfileEvent.VehicleEditColorChanged ->
                updateEdit { it.copy(color = event.value) }

            is ProfileEvent.VehicleEditPlateChanged ->
                updateEdit { it.copy(plate = event.value) }

            is ProfileEvent.VehicleEditSeatsChanged ->
                updateEdit { it.copy(seatsTotal = event.value) }

            is ProfileEvent.VehicleEditYearChanged ->
                updateEdit { it.copy(year = event.value) }

            is ProfileEvent.VehicleEditFunFactChanged ->
                updateEdit { it.copy(funFact = event.value) }

            ProfileEvent.SaveVehicleChanges -> saveVehicle()
        }
    }

    private fun updateEdit(transform: (VehicleEditState) -> VehicleEditState) {
        _uiState.value = _uiState.value.copy(
            vehicleEdit = transform(_uiState.value.vehicleEdit)
        )
    }

    private fun saveVehicle() {
        val state = _uiState.value
        val edit = state.vehicleEdit
        val id = edit.id ?: return

        val seats = edit.seatsTotal.toIntOrNull() ?: 0
        val year = edit.year.toIntOrNull() ?: 0

        val updatedVehicles = state.vehicles.map { v ->
            if (v.id == id) {
                v.copy(
                    make = edit.make,
                    model = edit.model,
                    color = edit.color,
                    plate = edit.plate,
                    seatsTotal = seats,
                    year = year,
                    funFact = edit.funFact
                )
            } else v
        }

        _uiState.value = state.copy(
            vehicles = updatedVehicles,
            isVehicleDialogOpen = false,
            vehicleEdit = VehicleEditState()
        )

        // later: also push this change to your SQLite DB here
    }
}
