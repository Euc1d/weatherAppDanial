package com.example.weatherappdanial.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherappdanial.data.local.entity.DailyForecastEntity
import com.example.weatherappdanial.data.local.entity.ForecastDetailsEntity
import com.example.weatherappdanial.data.local.entity.HourlyForecastEntity
import com.example.weatherappdanial.data.local.entity.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        HourlyForecastEntity::class,
        DailyForecastEntity::class,
        ForecastDetailsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}