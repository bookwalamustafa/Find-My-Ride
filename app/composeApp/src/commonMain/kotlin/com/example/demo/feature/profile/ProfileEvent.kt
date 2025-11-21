package com.example.demo.feature.profile

enum class SettingsOption {
    AccountSetting,
    Preferences,
    PrivacySetting,
}
sealed interface ProfileEvent {

    // Vehicles
    data class VehicleClicked(val vehicleId: Int) : ProfileEvent
    data object VehicleDialogDismissed : ProfileEvent
    data class VehicleEditMakeChanged(val value: String) : ProfileEvent
    data class VehicleEditModelChanged(val value: String) : ProfileEvent
    data class VehicleEditColorChanged(val value: String) : ProfileEvent
    data class VehicleEditPlateChanged(val value: String) : ProfileEvent
    data class VehicleEditSeatsChanged(val value: String) : ProfileEvent
    data class VehicleEditYearChanged(val value: String) : ProfileEvent
    data class VehicleEditFunFactChanged(val value: String) : ProfileEvent
    data object AddVehicleClicked : ProfileEvent
    data object DeleteVehicleClicked : ProfileEvent
    data object SaveVehicleChanges : ProfileEvent


    data class PreferencesChanged(
        val notificationsEnabled: Boolean,
        val emailUpdatesEnabled: Boolean,
        val darkModeEnabled: Boolean,
    ) : ProfileEvent

    data object SavePreferences : ProfileEvent

    // Settings
    data class SettingsClicked(val option: SettingsOption) : ProfileEvent
    data object SettingsDialogDismissed : ProfileEvent

    data class AccountSettingsChanged(
        val fullName: String? = null,
        val email: String? = null,
        val phone: String? = null,
        val password: String? = null,
    ) : ProfileEvent

    data object SaveAccountSettings : ProfileEvent
    data object DeleteAccount : ProfileEvent
}
