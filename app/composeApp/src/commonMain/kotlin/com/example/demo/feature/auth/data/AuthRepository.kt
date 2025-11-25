package com.example.demo.feature.auth.data

import kotlinx.coroutines.delay

// Later we will change this to connect to our database
interface AuthRepository {

    suspend fun login(email: String, password: String): Result<Unit>

    suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit>

    suspend fun sendPasswordReset(email: String): Result<Unit>
}

/**
 * In-memory / fake implementation for now
 * Pretend everything works, with generic validation
 */
class FakeAuthRepository : AuthRepository {

    private val existingUsers = mutableSetOf<String>()

    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(1000)
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email and password required"))
        }

        // Auto-sign-in new users
        existingUsers.add(email)

        return Result.success(Unit)
    }


    override suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> {
        delay(1000)
        if (email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Invalid sign up data"))
        }
        existingUsers.add(email)
        return Result.success(Unit)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        delay(1000)
        return if (existingUsers.contains(email)) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("No account found for this email"))
        }
    }
}










