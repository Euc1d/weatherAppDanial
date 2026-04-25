package com.example.weatherappdanial.domain.weather_model

data class ForeCastDetails(
    val windInfo: WindInfo,
    val sunrise: Long,
    val sunset: Long,
    val humidity: Int,
    val pressure: Int,
    val dewPoint: Double,
    val feelsLike: Double,
    val UvIndex: Int,
    val visibility: Int
)