package com.example.weatherappdanial.data.remote.api

import com.example.weatherappdanial.data.remote.dto.current.CurrentWeatherDTO
import com.example.weatherappdanial.data.remote.dto.onecall.WeatherResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/3.0/onecall?appid=aede904f2fcd352b09659db1cbceb9f7")
    suspend fun loadWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String = "ru",
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely,alerts"
    ): WeatherResponseDTO


    @GET("data/2.5/weather?appid=aede904f2fcd352b09659db1cbceb9f7")
    suspend fun getCurrentWeather(
        @Query("lat")   lat  : Double,
        @Query("lon")   lon  : Double,
        @Query("lang")  lang : String = "ru",
        @Query("units") units: String = "metric"
    ): CurrentWeatherDTO
}
