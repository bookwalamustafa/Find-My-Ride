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











