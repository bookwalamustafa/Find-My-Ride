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

            is ProfileEvent.SettingsClicked -> {
                _uiState.value = _uiState.value.copy(
                    isSettingsDialogOpen = true,
                    selectedSettingsOption = event.option
                )
            }

            is ProfileEvent.AddVehicleClicked -> {
                _uiState.value = _uiState.value.copy(
                    isVehicleDialogOpen = true,
                    vehicleEdit = VehicleEditState( // empty files = add more
                        id = null,
                        ownerUserId = 1, // or current user id later
                        make = "",
                        model = "",
                        color = "",
                        plate = "",
                        seatsTotal = "",
                        year = "",
                        funFact = "",
                    )
                )
            }

            is ProfileEvent.DeleteVehicleClicked -> {
                val edit = _uiState.value.vehicleEdit
                val id = edit.id ?: return // only valid when editing

                _uiState.value = _uiState.value.copy(
                    vehicles = _uiState.value.vehicles.filterNot { it.id == id },
                    isVehicleDialogOpen = false,
                    vehicleEdit = VehicleEditState()
                )
            }
            ProfileEvent.SettingsDialogDismissed -> {
                _uiState.value = _uiState.value.copy(
                    isSettingsDialogOpen = false,
                    selectedSettingsOption = null
                )
            }
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

        val seats = edit.seatsTotal.toIntOrNull() ?: 0
        val year = edit.year.toIntOrNull() ?: 0

        val updatedVehicles = if (edit.id == null) {
            // ---------- ADD NEW VEHICLE ---------- //
            val newId = (state.vehicles.maxOfOrNull { it.id } ?: 0) + 1

            state.vehicles + VehicleUi(
                id = newId,
                ownerUserId = edit.ownerUserId ?: 1,
                make = edit.make,
                model = edit.model,
                color = edit.color,
                plate = edit.plate,
                seatsTotal = seats,
                year = year,
                funFact = edit.funFact
            )
        } else {
            // ---------- EDIT EXISTING VEHICLE ---------- //
            state.vehicles.map { v ->
                if (v.id == edit.id) {
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
        }

        _uiState.value = state.copy(
            vehicles = updatedVehicles,
            isVehicleDialogOpen = false,
            vehicleEdit = VehicleEditState()
        )
    }

}
