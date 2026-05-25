package com.example.weatherappdanial.domain.usecases.city

import com.example.weatherappdanial.domain.city_model.CityLocation
import com.example.weatherappdanial.domain.repository.CityRepository

class DeleteCity (private val repo: CityRepository) {
    suspend operator fun invoke(city: CityLocation) = repo.deleteCity(city)
}