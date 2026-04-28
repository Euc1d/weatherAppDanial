package com.example.weatherappdanial.presentation.test


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappdanial.domain.result.LocationError
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.WeatherError
import com.example.weatherappdanial.domain.usecases.GetCurrentLocation
import com.example.weatherappdanial.domain.usecases.GetWeatherData
import com.example.weatherappdanial.domain.weather_model.WeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WeatherUiState {
    data object Idle : WeatherUiState
    data object LoadingLocation : WeatherUiState
    data object LoadingWeather : WeatherUiState
    data class Success(val data: WeatherData) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentLocation: GetCurrentLocation,
    private val getWeatherData: GetWeatherData
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.LoadingLocation

            when (val locationResult = getCurrentLocation()) {
                is Result.Success -> {
                    _uiState.value = WeatherUiState.LoadingWeather
                    getWeatherData(locationResult.data).collect { weatherResult ->
                        when (weatherResult) {
                            is Result.Success -> _uiState.value = WeatherUiState.Success(weatherResult.data)

                            is Result.Error -> {
                                when(weatherResult.error){
                                    WeatherError.SERVER_ERROR ->{

                                    }
                                    WeatherError.INTERNET_ERROR ->{
                                    }
                                    else -> {}
                                }
                            }
                        }
                    }
                }
                is Result.Error ->  when(locationResult.error){
                    LocationError.GPS_DISABLED ->{

                    }
                    else -> {

                    }
                }
            }
        }
    }
}