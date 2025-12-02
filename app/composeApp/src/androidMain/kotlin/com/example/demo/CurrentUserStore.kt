package com.example.demo

/**
 * Simple in-memory store for the currently logged-in user.
 * Android only; commonMain doesn't see this directly.
 */
object CurrentUserStore {
    var userId: Long? = null
    var email: String? = null
    var username: String? = null
    var role: String? = null
}

// This is just a global holder on the Android side.