package com.example.weatherappdanial.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherappdanial.data.local.entity.city.CityDao
import com.example.weatherappdanial.data.local.entity.city.SavedCityEntity
import com.example.weatherappdanial.data.local.entity.weather.DailyForecastEntity
import com.example.weatherappdanial.data.local.entity.weather.ForecastDetailsEntity
import com.example.weatherappdanial.data.local.entity.weather.HourlyForecastEntity
import com.example.weatherappdanial.data.local.entity.weather.WeatherDao
import com.example.weatherappdanial.data.local.entity.weather.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        HourlyForecastEntity::class,
        DailyForecastEntity::class,
        ForecastDetailsEntity::class,
        SavedCityEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class WeatherDataBase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun cityDao(): CityDao
}