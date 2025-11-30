package com.example.demo.feature.profile.data

import com.example.demo.CurrentUserStore
import com.example.demo.FindMyRideDbProvider
import com.example.demo.feature.profile.ProfileUiState
import com.example.demo.feature.profile.VehicleUi

/**
 * Android implementation of ProfileRepository that reads/writes
 * the USER and VEHICLE tables from findmyride.db
 */
class AndroidProfileRepository(
    private val dbProvider: FindMyRideDbProvider
) : ProfileRepository {

    private fun requireCurrentUserId(): Long {
        return CurrentUserStore.userId
            ?: error("No logged-in user. Make sure login ran successfully before opening Profile.")
    }

    override fun loadInitialProfile(): ProfileUiState {
        val currentUserId = requireCurrentUserId()
        val db = dbProvider.getReadableDatabase()

        // ----- USER -----
        var name = "Unknown"
        var email = ""
        var phone = ""
        var rating = 0.0

        db.rawQuery(
            """
            SELECT username, email, phone_number, rating_avg
            FROM "USER"
            WHERE user_id = ?
            LIMIT 1;
            """.trimIndent(),
            arrayOf(currentUserId.toString())
        ).use { c ->
            if (c.moveToFirst()) {
                name = c.getString(c.getColumnIndexOrThrow("username"))
                email = c.getString(c.getColumnIndexOrThrow("email"))
                phone = c.getString(c.getColumnIndexOrThrow("phone_number")) ?: ""
                rating = c.getDouble(c.getColumnIndexOrThrow("rating_avg"))
            }
        }

        // ----- VEHICLES -----
        val vehicles = mutableListOf<VehicleUi>()
        db.rawQuery(
            """
            SELECT vehicle_id, make, model, color, plate, seats_total, year, fun_fact
            FROM "VEHICLE"
            WHERE owner_user_id = ?
            ORDER BY vehicle_id;
            """.trimIndent(),
            arrayOf(currentUserId.toString())
        ).use { c ->
            val idxId        = c.getColumnIndexOrThrow("vehicle_id")
            val idxMake      = c.getColumnIndexOrThrow("make")
            val idxModel     = c.getColumnIndexOrThrow("model")
            val idxColor     = c.getColumnIndexOrThrow("color")
            val idxPlate     = c.getColumnIndexOrThrow("plate")
            val idxSeats     = c.getColumnIndexOrThrow("seats_total")
            val idxYear      = c.getColumnIndexOrThrow("year")
            val idxFunFact   = c.getColumnIndexOrThrow("fun_fact")

            while (c.moveToNext()) {
                vehicles += VehicleUi(
                    id = c.getInt(idxId),
                    ownerUserId = currentUserId.toInt(),
                    make = c.getString(idxMake),
                    model = c.getString(idxModel),
                    color = c.getString(idxColor) ?: "",
                    plate = c.getString(idxPlate),
                    seatsTotal = c.getInt(idxSeats),
                    year = c.getInt(idxYear),
                    funFact = c.getString(idxFunFact) ?: ""
                )
            }
        }

        // Build ProfileUiState using DB values
        return ProfileUiState(
            name = name,
            email = email,
            rating = rating,
            vehicles = vehicles,
            account = ProfileUiState().account.copy(
                fullName = name,
                email = email,
                phone = phone,
                // we don't show real password here
                password = "*******"
            )
        )
    }

    override fun saveProfile(state: ProfileUiState) {
        val currentUserId = requireCurrentUserId()
        val db = dbProvider.getWritableDatabase()
        db.beginTransaction()
        try {
            // ----- UPDATE USER -----
            db.compileStatement(
                """
                UPDATE "USER"
                SET username = ?, email = ?, phone_number = ?
                WHERE user_id = ?;
                """.trimIndent()
            ).apply {
                bindString(1, state.account.fullName)
                bindString(2, state.account.email)
                bindString(3, state.account.phone)
                bindLong(4, currentUserId)
                executeUpdateDelete()
            }

            // ----- SYNC VEHICLES -----
            // Get existing vehicle ids in DB
            val existingIds = mutableSetOf<Int>()
            db.rawQuery(
                """
                SELECT vehicle_id
                FROM "VEHICLE"
                WHERE owner_user_id = ?;
                """.trimIndent(),
                arrayOf(currentUserId.toString())
            ).use { c ->
                val idx = c.getColumnIndexOrThrow("vehicle_id")
                while (c.moveToNext()) existingIds += c.getInt(idx)
            }

            val desiredIds = state.vehicles.map { it.id }.toSet()

            // Upsert each vehicle from UI state
            state.vehicles.forEach { v ->
                if (existingIds.contains(v.id)) {
                    // UPDATE
                    db.compileStatement(
                        """
                        UPDATE "VEHICLE"
                        SET make = ?, model = ?, color = ?, plate = ?,
                            seats_total = ?, year = ?, fun_fact = ?
                        WHERE vehicle_id = ?;
                        """.trimIndent()
                    ).apply {
                        bindString(1, v.make)
                        bindString(2, v.model)
                        bindString(3, v.color)
                        bindString(4, v.plate)
                        bindLong(5, v.seatsTotal.toLong())
                        bindLong(6, v.year.toLong())
                        bindString(7, v.funFact)
                        bindLong(8, v.id.toLong())
                        executeUpdateDelete()
                    }
                } else {
                    // INSERT with explicit vehicle_id (matches the id created in ViewModel)
                    db.compileStatement(
                        """
                        INSERT INTO "VEHICLE"(
                            vehicle_id, owner_user_id, make, model, color,
                            plate, seats_total, year, fun_fact
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
                        """.trimIndent()
                    ).apply {
                        bindLong(1, v.id.toLong())
                        bindLong(2, currentUserId)
                        bindString(3, v.make)
                        bindString(4, v.model)
                        bindString(5, v.color)
                        bindString(6, v.plate)
                        bindLong(7, v.seatsTotal.toLong())
                        bindLong(8, v.year.toLong())
                        bindString(9, v.funFact)
                        executeInsert()
                    }
                }
            }

            // Delete vehicles that were removed in the UI
            (existingIds - desiredIds).forEach { idToDelete ->
                db.compileStatement(
                    """DELETE FROM "VEHICLE" WHERE vehicle_id = ?;""".trimIndent()
                ).apply {
                    bindLong(1, idToDelete.toLong())
                    executeUpdateDelete()
                }
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}
