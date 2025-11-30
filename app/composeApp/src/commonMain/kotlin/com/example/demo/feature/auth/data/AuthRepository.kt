package com.example.demo.feature.auth.data

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

    private val existingUsers = mutableSetOf<String>("john.doe@email.com")

    override suspend fun login(email: String, password: String): Result<Unit> {
        // super dumb fake logic here
        return if (existingUsers.contains(email) && password.isNotEmpty()) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("Invalid email or password"))
        }
    }

    override suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> {
        if (email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Invalid sign up data"))
        }
        existingUsers.add(email)
        return Result.success(Unit)
    }

    override suspend fun sendPasswordReset(email: String): Result<Unit> {
        return if (existingUsers.contains(email)) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalArgumentException("No account found for this email"))
        }
    }
}










