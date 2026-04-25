package com.example.weatherappdanial.data.remote.api

import com.example.weatherappdanial.data.remote.dto.WeatherResponseDTO
import com.example.weatherappdanial.domain.weather_model.WeatherData
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
        ): WeatherResponseDTO

}
/*
*https://api.openweathermap.org/data/3.0/onecall?lat=33.44&lon=-94.04&exclude=hourly,daily&appid=aede904f2fcd352b09659db1cbceb9f7
interface NewsApiService {

    @GET("v2/everything?apiKey=274e522e32b84233b5ff6041ded94631")  /ендпойнты можешь сразу писать те которые не будут меняться не забывай про правила выше
    suspend fun loadArticles(
        @Query("q") topic: String,
        @Query("languge") lang: String  /тупо перечисли ключь с дока и тем что ты хоч передать
    ): NewsResponseDTO  /дто елемент который получили самый мейн
}*/