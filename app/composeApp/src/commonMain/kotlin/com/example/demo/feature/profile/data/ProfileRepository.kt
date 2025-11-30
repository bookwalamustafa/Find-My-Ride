package com.example.demo.feature.profile.data

import com.example.demo.feature.profile.ProfileUiState

/**
 * This is our "data source" API.
 * Later you can have a real DB implementation that also implements this.
 */
interface ProfileRepository {
    fun loadInitialProfile(): ProfileUiState
    fun saveProfile(state: ProfileUiState)
}

/**
 * Simple in-memory implementation
 * Acts like a fake database for now.
 */
class InMemoryProfileRepository : ProfileRepository {

    // This is our "stored" profile (fake DB row)
    private var current: ProfileUiState = ProfileUiState()

    override fun loadInitialProfile(): ProfileUiState = current

    override fun saveProfile(state: ProfileUiState) {
        current = state
        println(" [InMemoryProfileRepository] ProfileRepository.loadInitialProfile() current: $current")
    }
}