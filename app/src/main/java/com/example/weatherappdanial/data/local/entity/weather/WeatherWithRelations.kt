package com.example.weatherappdanial.data.local.entity.weather

import androidx.room.Embedded
import androidx.room.Relation

data class WeatherWithRelations(
    @Embedded val weather: WeatherEntity,

    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val hourlyForecast: List<HourlyForecastEntity>,

    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val dailyForecast: List<DailyForecastEntity>,

    @Relation(
        parentColumn = "cityName",
        entityColumn = "cityName"
    )
    val forecastDetails: ForecastDetailsEntity?
)