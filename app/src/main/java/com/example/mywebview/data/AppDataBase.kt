package com.example.mywebview.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mywebview.data.model.HistoryEntity

@Database(
    entities = [HistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}