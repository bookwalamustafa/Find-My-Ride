package com.example.demo.feature.profile

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.demo.feature.profile.data.InMemoryProfileRepository
import com.example.demo.feature.profile.data.ProfileRepository

class ProfileViewModel(
    private val repository: ProfileRepository = InMemoryProfileRepository()
) {

    private val _uiState = MutableStateFlow(repository.loadInitialProfile())
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

            ProfileEvent.SaveVehicleChanges -> {
                saveVehicle()
                persist()          // save to repo
            }

            is ProfileEvent.AddVehicleClicked -> {
                _uiState.value = _uiState.value.copy(
                    isVehicleDialogOpen = true,
                    vehicleEdit = VehicleEditState(
                        id = null,
                        ownerUserId = 1, // TODO: replace with real user id later
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
                val id = edit.id ?: return

                _uiState.value = _uiState.value.copy(
                    vehicles = _uiState.value.vehicles.filterNot { it.id == id },
                    isVehicleDialogOpen = false,
                    vehicleEdit = VehicleEditState()
                )
                persist()          // save to repo
            }

            is ProfileEvent.SettingsClicked -> {
                _uiState.value = _uiState.value.copy(
                    isSettingsDialogOpen = true,
                    selectedSettingsOption = event.option
                )
            }

            ProfileEvent.SettingsDialogDismissed -> {
                _uiState.value = _uiState.value.copy(
                    isSettingsDialogOpen = false,
                    selectedSettingsOption = null
                )
            }

            // ---------- Preferences ----------

            is ProfileEvent.PreferencesChanged -> {
                _uiState.value = _uiState.value.copy(
                    preferences = _uiState.value.preferences.copy(
                        notificationsEnabled = event.notificationsEnabled,
                        emailUpdatesEnabled = event.emailUpdatesEnabled,
                        darkModeEnabled = event.darkModeEnabled
                    )
                )
            }

            ProfileEvent.SavePreferences -> {
                println("Preferences saved: ${_uiState.value.preferences}")
                persist()          // save to repo
            }

            // ---------- Account Settings ----------

            is ProfileEvent.AccountSettingsChanged -> {
                val current = _uiState.value.account
                _uiState.value = _uiState.value.copy(
                    account = current.copy(
                        fullName = event.fullName ?: current.fullName,
                        email = event.email ?: current.email,
                        phone = event.phone ?: current.phone,
                        password = event.password ?: current.password
                    )
                )
            }

            ProfileEvent.SaveAccountSettings -> {
                println("ACCOUNT UPDATED: ${_uiState.value.account}")
                persist()          // save to repo
            }

            ProfileEvent.DeleteAccount -> {
                println("ACCOUNT DELETED (TODO: real delete + logout)")
                // You might later clear profile / navigate away etc.
                persist()          // still log/save current state
            }

            // ---------- Privacy & Safety ----------

            is ProfileEvent.PrivacySafetyChanged -> {
                _uiState.value = _uiState.value.copy(
                    privacy = _uiState.value.privacy.copy(
                        showProfilePublicly = event.showProfilePublicly,
                        allowMessagesFromNonContacts = event.allowMessagesFromNonContacts,
                        shareTripHistoryWithFriends = event.shareTripHistoryWithFriends,
                        twoFactorEnabled = event.twoFactorEnabled
                    )
                )
            }

            ProfileEvent.SavePrivacySafety -> {
                println("Privacy & Safety saved: ${_uiState.value.privacy}")
                persist()          // save to repo
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

    // Single place to write to our "fake DB"
    private fun persist() {
        repository.saveProfile(_uiState.value)
    }
}
