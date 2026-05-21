package com.example.weatherappdanial.domain.city_model

data class CityLocation(
    val id : Int = 0,
    val displayName : String,
    val lat: Double,
    val lon: Double
)