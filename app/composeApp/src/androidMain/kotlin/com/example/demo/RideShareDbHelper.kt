package com.example.demo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RideShareDbHelper(context: Context) :
    SQLiteOpenHelper(context, "rideshare.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create a super simple table for now
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS rides(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pickup TEXT NOT NULL,
                dropoff TEXT NOT NULL,
                ride_time TEXT NOT NULL
            );
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Easiest upgrade strategy for school project
        db.execSQL("DROP TABLE IF EXISTS rides;")
        onCreate(db)
    }
}
