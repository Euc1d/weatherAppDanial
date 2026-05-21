package com.example.weatherappdanial.data.local.entity.weather

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val cityName: String,
    val currentTemp: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val todayDescription: String,
    val timezoneOffsetSec : Int  = 0,
    val cachedAt: Long = System.currentTimeMillis()
)