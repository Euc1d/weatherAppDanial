package com.example.weatherappdanial.data.remote.api

import com.example.weatherappdanial.data.remote.dto.WeatherResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

val apiEx = "&lat=39.099724&lon=-94.578331&dt=1643803200"
interface WeatherApiService {
    @GET("data/3.0/onecall?appid=aede904f2fcd352b09659db1cbceb9f7") //апи нужно позже хранить в билдКонфиг, в проде так и буду делать
    suspend fun loadWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "ru",
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,alerts"
    ): WeatherResponseDTO

}
