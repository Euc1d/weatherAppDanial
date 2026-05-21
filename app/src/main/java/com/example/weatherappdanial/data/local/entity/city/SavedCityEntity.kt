package com.example.weatherappdanial.data.local.entity.city

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_cities")
data class SavedCityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val displayName : String,
    val lat         : Double,
    val lon         : Double
)