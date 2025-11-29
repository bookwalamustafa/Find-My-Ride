package com.example.demo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.FileOutputStream

class FindMyRideDbProvider(private val context: Context) {
    private val dbName = "findmyride.db"

    private fun ensureDbCopied() {
        val dbFile = context.getDatabasePath(dbName)
        if (dbFile.exists()) return

        dbFile.parentFile?.mkdirs()

        context.assets.open(dbName).use { input ->
            FileOutputStream(dbFile).use { output ->
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
            SQLiteDatabase.OPEN_READONLY
        )
    }
}