package com.baloot.mirzazade.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ResponseDB::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun responseDb(): ResponseDao
}