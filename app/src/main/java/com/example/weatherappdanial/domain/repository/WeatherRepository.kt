package com.example.weatherappdanial.domain.repository

import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.RootError
import com.example.weatherappdanial.domain.weather_model.WeatherData

interface WeatherRepository {
    fun getWeatherData(location: Location): Result<WeatherData, RootError>

}