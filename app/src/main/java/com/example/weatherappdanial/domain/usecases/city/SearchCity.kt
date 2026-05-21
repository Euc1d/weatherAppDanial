package com.example.weatherappdanial.domain.usecases.city

import com.example.weatherappdanial.domain.repository.CityRepository
import javax.inject.Inject

class SearchCity @Inject constructor(private val repo: CityRepository) {
    suspend operator fun invoke(query: String) = repo.searchCity(query)
}