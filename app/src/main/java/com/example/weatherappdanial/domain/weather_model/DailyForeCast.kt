package com.example.weatherappdanial.domain.weather_model

data class DailyForeCast(
    val dayOfWeek: String,
    val icon: String,
    val minTemp: String,
    val maxTemp: String,
    val summary: String
)