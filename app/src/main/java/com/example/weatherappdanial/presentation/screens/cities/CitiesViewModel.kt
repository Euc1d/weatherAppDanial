package com.example.weatherappdanial.presentation.screens.cities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappdanial.domain.city_model.CityLocation
import com.example.weatherappdanial.domain.city_model.CitySummary
import com.example.weatherappdanial.domain.pref.TemperatureUnit
import com.example.weatherappdanial.domain.pref.UserPrefsRepository
import com.example.weatherappdanial.domain.repository.SearchError
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.usecases.city.DeleteCity
import com.example.weatherappdanial.domain.usecases.city.GetCitySummary
import com.example.weatherappdanial.domain.usecases.city.GetSavedCities
import com.example.weatherappdanial.domain.usecases.city.SaveCity
import com.example.weatherappdanial.domain.usecases.city.SearchCity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val getSavedCities: GetSavedCities,
    private val searchCity    : SearchCity,
    private val saveCity      : SaveCity,
    private val deleteCity    : DeleteCity,
    private val getCitySummary: GetCitySummary,
    private val prefsRepo      : UserPrefsRepository
) : ViewModel() {

    val cities: StateFlow<List<CityLocation>> =
        getSavedCities()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _summaries = MutableStateFlow<Map<Int, CitySummary>>(emptyMap())
    val summaries: StateFlow<Map<Int, CitySummary>> = _summaries.asStateFlow()

    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            cities.collect { list ->
                list.forEach { city ->
                    if (!_summaries.value.containsKey(city.id)) {
                        loadSummary(city)
                    }
                }
            }
        }
    }

    private fun loadSummary(city: CityLocation) {
        viewModelScope.launch {
            when (val r = getCitySummary(city)) {
                is Result.Success -> _summaries.update { it + (city.id to r.data) }
                is Result.Error   -> Unit
            }
        }
    }

    fun search(query: String) {
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.isBlank()) { _searchState.value = SearchUiState.Idle; return }

        searchJob = viewModelScope.launch {
            delay(400)
            _searchState.value = SearchUiState.Loading

            var lastError: SearchError = SearchError.UNKNOWN
            repeat(3) { attempt ->
                when (val r = searchCity(trimmed)) {
                    is Result.Success -> {
                        _searchState.value = if (r.data.isEmpty()) SearchUiState.Empty
                        else SearchUiState.Results(r.data)
                        return@launch
                    }
                    is Result.Error -> {
                        lastError = r.error
                        if (r.error != SearchError.NETWORK_ERROR) return@launch
                        if (attempt < 2) delay((attempt + 1) * 1000L)
                    }
                }
            }
            _searchState.value = SearchUiState.Error(lastError)
        }
    }

    fun save(city: CityLocation) {
        viewModelScope.launch {
            saveCity(city)
            loadSummary(city)
        }
    }

    fun delete(city: CityLocation) { viewModelScope.launch { deleteCity(city) } }

    fun resetSearch() {
        searchJob?.cancel()
        _searchState.value = SearchUiState.Idle
    }

    val temperatureUnit: StateFlow<TemperatureUnit> = prefsRepo.temperatureUnit
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TemperatureUnit.CELSIUS)

    fun setTempUnit(unit: TemperatureUnit) {
        viewModelScope.launch { prefsRepo.setTemperatureUnit(unit) }
    }
}

sealed interface SearchUiState {
    data object Idle                                  : SearchUiState
    data object Loading                               : SearchUiState
    data object Empty                                 : SearchUiState
    data class  Results(val data: List<CityLocation>) : SearchUiState
    data class  Error(val error: SearchError)         : SearchUiState
}