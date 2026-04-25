package com.example.weatherappdanial.domain.usecases

import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.repository.WeatherRepository
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.RootError
import com.example.weatherappdanial.domain.weather_model.WeatherData

class GetWeatherData(private val repository: WeatherRepository) {
    suspend operator fun invoke(location: Location): Result<WeatherData, RootError> {
        return repository.getWeatherData(location)
    }
}