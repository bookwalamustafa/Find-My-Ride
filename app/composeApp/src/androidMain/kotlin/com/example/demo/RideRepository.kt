package com.example.demo

import android.content.ContentValues

data class Ride(
    val id: Long,
    val pickup: String,
    val dropoff: String,
    val time: String
)

class RideRepository(private val dbHelper: RideShareDbHelper) {

    fun insertRide(pickup: String, dropoff: String, time: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("pickup", pickup)
            put("dropoff", dropoff)
            put("ride_time", time)
        }
        return db.insert("rides", null, values)
    }

    fun getAllRides(): List<Ride> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, pickup, dropoff, ride_time FROM rides ORDER BY id DESC",
            null
        )

        val rides = mutableListOf<Ride>()
        cursor.use {
            val idxId = it.getColumnIndexOrThrow("id")
            val idxPickup = it.getColumnIndexOrThrow("pickup")
            val idxDropoff = it.getColumnIndexOrThrow("dropoff")
            val idxTime = it.getColumnIndexOrThrow("ride_time")

            while (it.moveToNext()) {
                rides.add(
                    Ride(
                        id = it.getLong(idxId),
                        pickup = it.getString(idxPickup),
                        dropoff = it.getString(idxDropoff),
                        time = it.getString(idxTime)
                    )
                )
            }
        }
        return rides
    }
}
