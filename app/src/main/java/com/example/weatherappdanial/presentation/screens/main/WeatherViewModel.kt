package com.example.weatherappdanial.presentation.screens.main

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

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getLocation   : GetCurrentLocation,
    private val getWeatherData: GetWeatherData,
) : ViewModel() {

        private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
        val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

        private var job: kotlinx.coroutines.Job? = null

        init { loadWeather() }

        fun loadWeather() {
            job?.cancel()
            job = viewModelScope.launch {
                val hadContent = _uiState.value is WeatherUiState.Content
                if (!hadContent) _uiState.value = WeatherUiState.Loading

                val location = when (val r = getLocation()) {
                    is Result.Success -> r.data
                    is Result.Error   -> {
                        _uiState.value = when (r.error) {
                            LocationError.NO_PERMISSION -> WeatherUiState.FullScreenError.NoPermission
                            LocationError.GPS_DISABLED  -> WeatherUiState.FullScreenError.GpsDisabled
                            LocationError.UNKNOWN       -> WeatherUiState.FullScreenError.Unknown
                        }
                        return@launch
                    }
                }

                var pendingBanner: WeatherBanner? = null

                getWeatherData(location).collect { result ->
                    when (result) {
                        is Result.Error   -> {
                            pendingBanner = when (result.error) {
                                WeatherError.INTERNET_ERROR -> WeatherBanner.NoInternet
                                else                        -> WeatherBanner.ServerError
                            }
                            if (!hadContent && _uiState.value !is WeatherUiState.Content) {
                                _uiState.value = when (result.error) {
                                    WeatherError.INTERNET_ERROR -> WeatherUiState.FullScreenError.NoInternet
                                    WeatherError.SERVER_ERROR,
                                    WeatherError.NETWORK_ERROR  -> WeatherUiState.FullScreenError.ServerError
                                    WeatherError.UNKNOWN        -> WeatherUiState.FullScreenError.Unknown
                                }
                            } else {
                                (_uiState.value as? WeatherUiState.Content)
                                    ?.let { _uiState.value = it.copy(banner = pendingBanner) }
                            }
                        }
                        is Result.Success -> {
                            _uiState.value = WeatherUiState.Content(result.data, pendingBanner)
                            pendingBanner  = null
                        }
                    }
                }
            }
        }

        fun dismissBanner() {
            (_uiState.value as? WeatherUiState.Content)
                ?.let { _uiState.value = it.copy(banner = null) }
        }
    }

sealed interface WeatherUiState {

    data object Loading : WeatherUiState

    data class Content(
        val data: WeatherData,
        val banner: WeatherBanner? = null
    ) : WeatherUiState

    sealed interface FullScreenError : WeatherUiState {
        data object NoPermission : FullScreenError  // LocationError.NO_PERMISSION
        data object GpsDisabled  : FullScreenError  // LocationError.GPS_DISABLED
        data object NoInternet   : FullScreenError  // WeatherError.INTERNET_ERROR
        data object ServerError  : FullScreenError  // WeatherError.SERVER/NETWORK
        data object Unknown      : FullScreenError
    }
}

sealed interface WeatherBanner {
    data object NoInternet  : WeatherBanner
    data object ServerError : WeatherBanner
}