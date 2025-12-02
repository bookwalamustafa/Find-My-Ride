package com.example.demo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.FileOutputStream

class FindMyRideDbProvider(private val context: Context) {
    private val dbName = "findmyride.db"

    private fun ensureDbCopied() {
        val dbFile = context.getDatabasePath(dbName)

        // Always overwrite old database to ensure latest version is used
        dbFile.parentFile?.mkdirs()

        // Delete any existing DB
        if (dbFile.exists()) {
            dbFile.delete()
        }

        // Copy fresh DB from assets
        context.assets.open(dbName).use { input ->
            dbFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getReadableDatabase(): SQLiteDatabase {
        ensureDbCopied()
        val dbFile = context.getDatabasePath(dbName)
        return SQLiteDatabase.openDatabase(
            dbFile.path,
            null,
            SQLiteDatabase.OPEN_READONLY
        )
    }

    fun getWritableDatabase(): SQLiteDatabase {
        ensureDbCopied()
        val dbFile = context.getDatabasePath(dbName)
        return SQLiteDatabase.openDatabase(
            dbFile.path,
            null,
            SQLiteDatabase.OPEN_READWRITE
        )
    }
}