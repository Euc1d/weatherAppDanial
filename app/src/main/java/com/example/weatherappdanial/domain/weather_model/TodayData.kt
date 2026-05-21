package com.example.weatherappdanial.domain.weather_model

data class TodayData(
    val cityName: String,
    val currentTemp: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val todayDescription: String,
    val avgMaxTemp : Int,
    val diffFromAvgMax: Int,
    val timezoneOffsetSec: Int = 0
)