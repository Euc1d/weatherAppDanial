package com.example.weatherappdanial.data.local.entity.weather

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.weatherappdanial.data.local.entity.weather.WeatherEntity

@Entity(
    tableName = "hourly_forecast",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityName"],
        childColumns = ["cityName"],
        onDelete = ForeignKey.Companion.CASCADE
    )],
    indices = [Index("cityName")]
)
data class HourlyForecastEntity(
    @PrimaryKey
    val timeStamp: Long,
    val cityName: String,
    val description: String,
    val temperature: Int,
    val icon: String
)