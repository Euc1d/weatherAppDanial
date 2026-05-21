package com.example.weatherappdanial.presentation.screens.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.pref.TemperatureUnit
import com.example.weatherappdanial.domain.pref.UserPrefsRepository
import com.example.weatherappdanial.domain.result.LocationError
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.WeatherError
import com.example.weatherappdanial.domain.usecases.GetCurrentLocation
import com.example.weatherappdanial.domain.usecases.GetWeatherData
import com.example.weatherappdanial.domain.weather_model.WeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getLocation: GetCurrentLocation,
    private val getWeatherData: GetWeatherData,
    private val prefsRepo: UserPrefsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val navLat: Float = savedStateHandle["lat"] ?: 0f
    private val navLon: Float = savedStateHandle["lon"] ?: 0f
    private val useGps: Boolean = navLat == 0f && navLon == 0f

    private var job: kotlinx.coroutines.Job? = null

    init { loadWeather() }

    fun loadWeather() {
        job?.cancel()
        job = viewModelScope.launch {
            val hadContent = _uiState.value is WeatherUiState.Content
            if (!hadContent) _uiState.value = WeatherUiState.Loading

            val location = if (useGps) {
                when (val r = getLocation()) {
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
            } else {
                Location(navLat.toDouble(), navLon.toDouble())
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

    val temperatureUnit: StateFlow<TemperatureUnit> = prefsRepo.temperatureUnit
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TemperatureUnit.CELSIUS)
}

sealed interface WeatherUiState {

    data object Loading : WeatherUiState

    data class Content(
        val data: WeatherData,
        val banner: WeatherBanner? = null
    ) : WeatherUiState

    sealed interface FullScreenError : WeatherUiState {
        data object NoPermission : FullScreenError
        data object GpsDisabled  : FullScreenError
        data object NoInternet   : FullScreenError
        data object ServerError  : FullScreenError
        data object Unknown      : FullScreenError
    }
}

sealed interface WeatherBanner {
    data object NoInternet  : WeatherBanner
    data object ServerError : WeatherBanner
}