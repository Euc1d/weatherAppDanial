package com.example.weatherappdanial.data.repository

import android.content.Context
import androidx.core.content.edit
import com.example.weatherappdanial.domain.pref.TemperatureUnit
import com.example.weatherappdanial.domain.pref.UserPrefsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class UserPrefsRepositoryImpl (
    context: Context
) : UserPrefsRepository {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override val temperatureUnit: Flow<TemperatureUnit> = flow {
        while (true) {
            val saved = prefs.getString("temp_unit", TemperatureUnit.CELSIUS.name)
            emit(TemperatureUnit.valueOf(saved ?: TemperatureUnit.CELSIUS.name))
            delay(500)
        }
    }.distinctUntilChanged()

    override suspend fun setTemperatureUnit(unit: TemperatureUnit) {
        prefs.edit { putString("temp_unit", unit.name) }
    }
}