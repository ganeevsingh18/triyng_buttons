package com.example.triyngbuttons.ui.theme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.triyngbuttons.StationEntity
import kotlinx.coroutines.flow.Flow
import kotlin.collections.List


@Dao
interface StationDao {
    @Insert
    fun insert(station: StationEntity)

    @Query("SELECT * FROM station_table")
    fun getAllStation(): List<StationEntity>
//    @Query("SELECT * FROM station_table")
//    fun getAllStations(): Flow<List<StationEntity>>
}
