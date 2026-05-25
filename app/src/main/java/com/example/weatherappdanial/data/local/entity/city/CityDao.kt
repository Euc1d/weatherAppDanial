package com.example.weatherappdanial.data.local.entity.city

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM saved_cities")
    fun getCities(): Flow<List<SavedCityEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(city: SavedCityEntity)

    @Delete
    suspend fun delete(city: SavedCityEntity)

    @Query("SELECT COUNT(*) FROM saved_cities WHERE lat = :lat AND lon = :lon")
    suspend fun exists(lat: Double, lon: Double): Int
}