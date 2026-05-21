package com.example.weatherappdanial.domain.usecases.city

import com.example.weatherappdanial.domain.repository.CityRepository
import javax.inject.Inject

class GetSavedCities @Inject constructor(private val repo: CityRepository) {
    operator fun invoke() = repo.getSavedCities()
}