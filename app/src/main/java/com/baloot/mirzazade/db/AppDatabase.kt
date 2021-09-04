package com.baloot.mirzazade.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baloot.mirzazade.model.NewsItem

@Database(entities = [ResponseDB::class,NewsItemDB::class], version = 7)
abstract class AppDatabase : RoomDatabase() {
    abstract fun responseDb(): ResponseDao
    abstract fun newsItem():NewsItemDao
}