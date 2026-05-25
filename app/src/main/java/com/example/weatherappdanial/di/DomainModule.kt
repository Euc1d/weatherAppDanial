package com.example.weatherappdanial.di

import com.example.weatherappdanial.domain.usecases.GetCurrentLocation
import com.example.weatherappdanial.domain.usecases.GetWeatherData
import com.example.weatherappdanial.domain.usecases.city.DeleteCity
import com.example.weatherappdanial.domain.usecases.city.GetCitySummary
import com.example.weatherappdanial.domain.usecases.city.GetSavedCities
import com.example.weatherappdanial.domain.usecases.city.SaveCity
import com.example.weatherappdanial.domain.usecases.city.SearchCity
import com.example.weatherappdanial.presentation.screens.cities.CitiesViewModel
import com.example.weatherappdanial.presentation.screens.main.WeatherViewModel
import org.koin.core.module.dsl.viewModel        // ← правильный импорт
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetCurrentLocation(get()) }
    factory { GetWeatherData(get()) }
    factory { SearchCity(get()) }
    factory { SaveCity(get()) }
    factory { DeleteCity(get()) }
    factory { GetSavedCities(get()) }
    factory { GetCitySummary(get()) }
}

val viewModelModule = module {
    viewModel { params ->
        WeatherViewModel(
            getLocation      = get(),
            getWeatherData   = get(),
            prefsRepo        = get(),
            savedStateHandle = params.get()
        )
    }
    viewModel {
        CitiesViewModel(
            getSavedCities = get(),
            searchCity     = get(),
            saveCity       = get(),
            deleteCity     = get(),
            getCitySummary = get(),
            prefsRepo      = get()
        )
    }
}