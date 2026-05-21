package com.example.weatherappdanial.data.local.entity.weather

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.weatherappdanial.data.local.entity.weather.WeatherEntity


@Entity(
    tableName = "forecast_details",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityName"],
        childColumns = ["cityName"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cityName")]
)
data class ForecastDetailsEntity(
    @PrimaryKey
    val cityName: String,
    @Embedded val windInfo: WindInfoEmbedded,
    val sunrise: Long,
    val sunset: Long,
    val humidity: Int,
    val pressure: Int,
    val dewPoint: Double,
    val feelsLike: Double,
    val uvIndex: Int,
    val visibility: Int
)

data class WindInfoEmbedded(
    val speedKmh: Int,
    val gustKmh: Int,
    val directionDeg: Int
)
