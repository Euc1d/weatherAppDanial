package com.example.weatherappdanial.domain.result

enum class WeatherError : Error {
    NETWORK_ERROR,
    SERVER_ERROR,
    INTERNET_ERROR,
    UNKNOWN
}