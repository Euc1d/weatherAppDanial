package com.example.weatherappdanial.domain.pref

import kotlinx.coroutines.flow.Flow

interface UserPrefsRepository {
    val temperatureUnit: Flow<TemperatureUnit>
    suspend fun setTemperatureUnit(unit: TemperatureUnit)
}