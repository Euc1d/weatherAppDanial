package com.example.weatherappdanial.domain.city_model

data class CitySummary(
    val city       : CityLocation,
    val currentTemp: Int,
    val minTemp    : Int,
    val maxTemp    : Int,
    val description: String,
    val icon       : String,
    val updatedAt  : Long,
    val timezoneOffsetSec : Int = 0
)