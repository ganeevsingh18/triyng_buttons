package com.example.triyngbuttons

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.triyngbuttons.ui.theme.StationDao

@Database(entities = [StationEntity::class], version = 1, exportSchema = false)

abstract class StationDatabase : RoomDatabase() {
    abstract fun stationDao(): StationDao
}
