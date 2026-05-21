package com.example.weatherappdanial.domain.repository

import com.example.weatherappdanial.domain.city_model.CityLocation
import com.example.weatherappdanial.domain.city_model.CitySummary
import com.example.weatherappdanial.domain.result.Error
import com.example.weatherappdanial.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun searchCity(query: String): Result<List<CityLocation>, SearchError>
    fun getSavedCities(): Flow<List<CityLocation>>
    suspend fun saveCity(city: CityLocation)
    suspend fun deleteCity(city: CityLocation)
    suspend fun getCitySummary(city: CityLocation): Result<CitySummary, SearchError>
}

enum class SearchError : Error {
    NO_RESULTS,
    NETWORK_ERROR,
    UNKNOWN
}