package com.example.triyngbuttons
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_table")
data class StationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val distance: Int
)
