package com.example.demo

import android.content.ContentValues
import com.example.demo.feature.db.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidRideRepository(
    private val dbProvider: FindMyRideDbProvider
) : RideRepository {

    override suspend fun getOpenRideOffers(): List<RideOffer> =
        withContext(Dispatchers.IO) {
            val db = dbProvider.getReadableDatabase()

            // Example: join RIDE_OFFER with LOCATION to get names
            val sql = """
                SELECT 
                    o.offer_id,
                    o.driver_id,
                    o.vehicle_id,
                    lo_from.name AS from_name,
                    lo_to.name   AS to_name,
                    o.depart_at,
                    o.seats_available,
                    o.price_base
                FROM RIDE_OFFER o
                JOIN LOCATION lo_from ON o.original_location_id = lo_from.location_id
                JOIN LOCATION lo_to   ON o.dest_location_id     = lo_to.location_id
                WHERE o.status = 'open'
                ORDER BY o.depart_at ASC;
            """.trimIndent()

            val cursor = db.rawQuery(sql, null)

            val list = mutableListOf<RideOffer>()
            cursor.use {
                val idxOfferId       = it.getColumnIndexOrThrow("offer_id")
                val idxDriverId      = it.getColumnIndexOrThrow("driver_id")
                val idxVehicleId     = it.getColumnIndexOrThrow("vehicle_id")
                val idxFromName      = it.getColumnIndexOrThrow("from_name")
                val idxToName        = it.getColumnIndexOrThrow("to_name")
                val idxDepartAt      = it.getColumnIndexOrThrow("depart_at")
                val idxSeatsAvail    = it.getColumnIndexOrThrow("seats_available")
                val idxPriceBase     = it.getColumnIndexOrThrow("price_base")

                while (it.moveToNext()) {
                    list.add(
                        RideOffer(
                            offerId        = it.getLong(idxOfferId),
                            driverId       = it.getLong(idxDriverId),
                            vehicleId      = it.getLong(idxVehicleId),
                            fromName       = it.getString(idxFromName),
                            toName         = it.getString(idxToName),
                            departAt       = it.getString(idxDepartAt),
                            seatsAvailable = it.getInt(idxSeatsAvail),
                            priceBase      = it.getDouble(idxPriceBase)
                        )
                    )
                }
            }
            list
        }

    override suspend fun createRideRequest(
        riderId: Long,
        pickupLocationId: Long,
        dropoffLocationId: Long,
        earliestPickup: String,
        latestPickup: String?,
        seatsNeeded: Int
    ) {
        withContext(Dispatchers.IO) {
            val db = dbProvider.getWritableDatabase()
            val values = ContentValues().apply {
                put("rider_id", riderId)
                put("pickup_location_id", pickupLocationId)
                put("dropoff_location_id", dropoffLocationId)
                put("earliest_pickup", earliestPickup)
                put("latest_pickup", latestPickup)
                put("seats_needed", seatsNeeded)
                put("status", "open")
            }
            db.insert("RIDE_REQUEST", null, values)
        }
    }
}
