package com.example.weatherappdanial.domain.weather_model

data class HourlyForeCast(
    val description: String,
    val timeStamp: Long,
    val temperature: Int,
    val icon: String
)