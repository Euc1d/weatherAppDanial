package com.example.weatherappdanial.data.remote.dto.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Daily(
    @SerialName("clouds") val clouds: Int = 0,
    @SerialName("dew_point") val dewPoint: Double = 0.0,
    @SerialName("dt") val dt: Int = 0,
    @SerialName("feels_like") val feelsLike: FeelsLike = FeelsLike(),
    @SerialName("humidity") val humidity: Int = 0,
    @SerialName("moon_phase") val moonPhase: Double = 0.0,
    @SerialName("moonrise") val moonrise: Int = 0,
    @SerialName("moonset") val moonset: Int = 0,
    @SerialName("pop") val pop: Double = 0.0,
    @SerialName("pressure") val pressure: Int = 0,
    @SerialName("rain") val rain: Double? = null,
    @SerialName("summary") val summary: String = "",
    @SerialName("sunrise") val sunrise: Int = 0,
    @SerialName("sunset") val sunset: Int = 0,
    @SerialName("temp") val temp: Temp = Temp(),
    @SerialName("uvi") val uvi: Double = 0.0,
    @SerialName("weather") val weather: List<Weather> = listOf(),
    @SerialName("wind_deg") val windDeg: Int = 0,
    @SerialName("wind_gust") val windGust: Double = 0.0,
    @SerialName("wind_speed") val windSpeed: Double = 0.0
)