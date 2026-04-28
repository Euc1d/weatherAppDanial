package com.example.weatherappdanial.data.repository

import android.util.Log
import com.example.weatherappdanial.data.local.WeatherDao
import com.example.weatherappdanial.data.mapper.toDailyEntity
import com.example.weatherappdanial.data.mapper.toDetailsEntity
import com.example.weatherappdanial.data.mapper.toDomain
import com.example.weatherappdanial.data.mapper.toHourlyEntity
import com.example.weatherappdanial.data.mapper.toWeatherEntity
import com.example.weatherappdanial.data.remote.api.WeatherApiService
import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.repository.WeatherRepository
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.RootError
import com.example.weatherappdanial.domain.result.WeatherError
import com.example.weatherappdanial.domain.weather_model.WeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val dao: WeatherDao,
    private val api: WeatherApiService
) : WeatherRepository {

    override fun getWeatherData(location: Location): Flow<Result<WeatherData, WeatherError>> =
        channelFlow {


            val cityName = try {
                val dto = api.loadWeatherData(location.lat, location.lon)

                val city = dto.timezone
                    .substringAfterLast("/")
                    .replace("_", " ")

                dao.insertFullWeather(
                    weather = dto.toWeatherEntity(city),
                    hourly = dto.hourly.take(24).map { it.toHourlyEntity(city) },
                    daily = dto.daily.take(7).map { it.toDailyEntity(city) },
                    details = dto.toDetailsEntity(city)
                )
                city
            } catch (e: UnknownHostException) {
                send(Result.Error(WeatherError.INTERNET_ERROR))
                null
            } catch (e: SocketTimeoutException) {
                send(Result.Error(WeatherError.NETWORK_ERROR))
                null
            } catch (e: ConnectException) {
                send(Result.Error(WeatherError.NETWORK_ERROR))
                null
            } catch (e: HttpException) {
                send(Result.Error(WeatherError.SERVER_ERROR))
                null
            } catch (e: Exception) {
                send(Result.Error(WeatherError.UNKNOWN))
                null
            }

            val dbFlow = if (cityName != null) {
                dao.getWeatherData(cityName)
            } else {
                dao.getLatestWeather()
            }

            dbFlow.collectLatest { relations ->
                if (relations != null) {
                    send(Result.Success(relations.toDomain()))
                }
            }
        }
}