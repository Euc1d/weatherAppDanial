package com.example.weatherappdanial.domain.repository

import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.RootError
import com.example.weatherappdanial.domain.result.WeatherError
import com.example.weatherappdanial.domain.weather_model.WeatherData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherData(location: Location): Flow<Result<WeatherData, WeatherError>>

}