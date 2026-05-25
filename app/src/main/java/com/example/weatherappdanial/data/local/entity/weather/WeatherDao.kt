package com.example.weatherappdanial.data.local.entity.weather

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Transaction
    @Query("SELECT * FROM weather WHERE cityName = :cityName")
    fun getWeatherData(cityName: String): Flow<WeatherWithRelations?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(items: List<HourlyForecastEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(items: List<DailyForecastEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastDetails(details: ForecastDetailsEntity)

    @Transaction
    @Query("SELECT * FROM weather ORDER BY cachedAt DESC LIMIT 1")
    fun getLatestWeather(): Flow<WeatherWithRelations?>

    @Transaction
    suspend fun insertFullWeather(
        weather: WeatherEntity,
        hourly: List<HourlyForecastEntity>,
        daily: List<DailyForecastEntity>,
        details: ForecastDetailsEntity
    ) {
        insertWeather(weather)
        insertHourlyForecast(hourly)
        insertDailyForecast(daily)
        insertForecastDetails(details)
    }
}