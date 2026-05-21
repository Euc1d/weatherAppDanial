package com.example.weatherappdanial.data.repository

import android.util.Log
import com.example.weatherappdanial.data.local.entity.city.CityDao
import com.example.weatherappdanial.data.local.entity.city.SavedCityEntity
import com.example.weatherappdanial.data.remote.api.NominatimApiService
import com.example.weatherappdanial.data.remote.api.WeatherApiService
import com.example.weatherappdanial.data.remote.dto.location.LocationDTO
import com.example.weatherappdanial.domain.city_model.CityLocation
import com.example.weatherappdanial.domain.city_model.CitySummary
import com.example.weatherappdanial.domain.repository.CityRepository
import com.example.weatherappdanial.domain.repository.SearchError
import com.example.weatherappdanial.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.roundToInt

class CityRepositoryImpl @Inject constructor(
    private val weatherApi  : WeatherApiService,
    private val nominatimApi: NominatimApiService,
    private val dao         : CityDao
) : CityRepository {

    override suspend fun getCitySummary(city: CityLocation): Result<CitySummary, SearchError> =
        try {
            val dto = weatherApi.getCurrentWeather(city.lat, city.lon)
            Result.Success(
                CitySummary(
                    city        = city,
                    currentTemp = dto.main.temp.roundToInt(),
                    minTemp     = dto.main.tempMin.roundToInt(),
                    maxTemp     = dto.main.tempMax.roundToInt(),
                    description = dto.weather.firstOrNull()?.description.orEmpty(),
                    icon        = dto.weather.firstOrNull()?.icon.orEmpty(),
                    updatedAt   = dto.dt,
                    timezoneOffsetSec = dto.timezone
                )
            )
        } catch (e: UnknownHostException) { Result.Error(SearchError.NETWORK_ERROR) }
        catch (e: Exception)            { Result.Error(SearchError.UNKNOWN) }

    override suspend fun searchCity(query: String): Result<List<CityLocation>, SearchError> =
        try {
            val results = nominatimApi.getCoordinates(query)
            if (results.isEmpty()) Result.Success(emptyList())
            else Result.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            when (e) {
                is UnknownHostException, is SocketTimeoutException, is ConnectException ->
                    Result.Error(SearchError.NETWORK_ERROR)
                else -> Result.Error(SearchError.UNKNOWN)
            }
        }
    override fun getSavedCities(): Flow<List<CityLocation>> =
        dao.getCities().map { list -> list.map { it.toDomain() } }

    override suspend fun saveCity(city: CityLocation)   = dao.insert(city.toEntity())
    override suspend fun deleteCity(city: CityLocation) = dao.delete(city.toEntity())
}

private fun LocationDTO.toDomain() = CityLocation(
    displayName = display_name.substringBefore(",").trim(),
    lat         = lat.toDouble(),
    lon         = lon.toDouble()
)
private fun SavedCityEntity.toDomain() = CityLocation(id, displayName, lat, lon)
private fun CityLocation.toEntity()   = SavedCityEntity(id, displayName, lat, lon)