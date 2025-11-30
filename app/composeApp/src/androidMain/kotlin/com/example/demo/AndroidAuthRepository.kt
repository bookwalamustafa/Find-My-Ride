package com.example.demo

import app.composeapp.generated.resources.Res
import com.example.demo.feature.auth.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android implementation of AuthRepository that talks to the findmyride.db SQLite database.
 */
class AndroidAuthRepository(
    private val dbProvider: FindMyRideDbProvider
) : AuthRepository {


    override suspend fun login(email: String, password: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getReadableDatabase()

            val cursor = db.rawQuery(
                """
                SELECT user_id, email, username, role 
                FROM "USER"
                WHERE email = ? AND password_hash = ?
                """.trimIndent(),
                arrayOf(email, password)
            )

            cursor.use {
                return@withContext if (it.moveToFirst()) {
                    val idxId       = it.getColumnIndexOrThrow("user_id")
                    val idxEmail    = it.getColumnIndexOrThrow("email")
                    val idxUsername = it.getColumnIndexOrThrow("username")
                    val idxRole     = it.getColumnIndexOrThrow("role")

                    // Remember who is logged in
                    CurrentUserStore.userId     = it.getLong(idxId)
                    CurrentUserStore.email      = it.getString(idxEmail)
                    CurrentUserStore.username   = it.getString(idxUsername)
                    CurrentUserStore.role       = it.getString(idxRole)

                    Result.success(Unit)
                } else {
                    // Clear any previous user
                    CurrentUserStore.userId     = null
                    CurrentUserStore.email      = null
                    CurrentUserStore.username   = null
                    CurrentUserStore.role       = null

                    Result.failure(IllegalArgumentException("Invalid email or password"))
                }
            }
        }

    // Basic implementation so Sign Up / Forgot Password don't crash,
    // we can improve these later.
    override suspend fun signUp(
        fullName: String,
        email: String,
        password: String
    ): Result<Unit> =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getWritableDatabase()

            // Check if email already exists
            db.rawQuery(
                """SELECT 1 FROM "USER" WHERE email = ? LIMIT 1;""",
                arrayOf(email)
            ).use { c ->
                if (c.moveToFirst()) {
                    return@withContext Result.failure(
                        IllegalArgumentException("Email already registered")
                    )
                }
            }

            val username = if (fullName.isNotBlank()) {
                fullName.trim().lowercase().replace(" ", "_")
            } else {
                email.substringBefore('@')
            }

            val stmt = db.compileStatement(
                """
                INSERT INTO "USER"(email, username, password_hash, role)
                VALUES(?, ?, ?, 'rider');
                """.trimIndent()
            )
            stmt.bindString(1, email)
            stmt.bindString(2, username)
            stmt.bindString(3, password)

            val rowId = stmt.executeInsert()
            if (rowId == -1L) {
                Result.failure(IllegalStateException("Failed to create account"))
            } else {
                Result.success(Unit)
            }
        }

    override suspend fun sendPasswordReset(email: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getReadableDatabase()
            db.rawQuery(
                """SELECT 1 FROM "USER" WHERE email = ? LIMIT 1;""",
                arrayOf(email)
            ).use { c ->
                return@withContext if (c.moveToFirst()) {
                    // Pretend an email was sent
                    Result.success(Unit)
                } else {
                    Result.failure(IllegalArgumentException("No account found for this email"))
                }
            }
        }
}
