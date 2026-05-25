package com.example.weatherappdanial.domain.usecases.city

import com.example.weatherappdanial.domain.repository.CityRepository

class SearchCity (private val repo: CityRepository) {
    suspend operator fun invoke(query: String) = repo.searchCity(query)
}