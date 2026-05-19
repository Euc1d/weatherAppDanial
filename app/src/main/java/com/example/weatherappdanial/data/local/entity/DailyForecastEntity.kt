package com.example.weatherappdanial.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_forecast",
    foreignKeys = [ForeignKey(
        entity = WeatherEntity::class,
        parentColumns = ["cityName"],
        childColumns = ["cityName"],
        onDelete = CASCADE
    )],
    indices = [Index("cityName")]
)
data class DailyForecastEntity(
    @PrimaryKey
    val dayOfWeek: String,
    val cityName: String,
    val icon: String,
    val minTemp: String,
    val maxTemp: String,
    @ColumnInfo(defaultValue = "") val summary: String = ""
)