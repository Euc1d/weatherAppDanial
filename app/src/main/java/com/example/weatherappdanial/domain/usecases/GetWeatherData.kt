package com.example.weatherappdanial.domain.usecases

import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.repository.WeatherRepository
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.RootError
import com.example.weatherappdanial.domain.result.WeatherError
import com.example.weatherappdanial.domain.weather_model.WeatherData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherData @Inject constructor(private val repository: WeatherRepository) {
    suspend operator fun invoke(location: Location): Flow<Result<WeatherData, WeatherError>> {
        return repository.getWeatherData(location)
    }
}