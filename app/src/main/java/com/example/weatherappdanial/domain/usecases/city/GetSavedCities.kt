package com.example.weatherappdanial.domain.usecases.city

import com.example.weatherappdanial.domain.repository.CityRepository

class GetSavedCities (private val repo: CityRepository) {
    operator fun invoke() = repo.getSavedCities()
}