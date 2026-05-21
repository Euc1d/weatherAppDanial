package com.example.weatherappdanial.data.remote.dto.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponseDTO(
    @SerialName("current")
    val current: Current = Current(),
    @SerialName("daily")
    val daily: List<Daily> = listOf(),
    @SerialName("hourly")
    val hourly: List<Hourly> = listOf(),
    @SerialName("lat")
    val lat: Double = 0.0,
    @SerialName("lon")
    val lon: Double = 0.0,
    @SerialName("minutely")
    val minutely: List<Minutely> = listOf(),
    @SerialName("timezone")
    val timezone: String = "",
    @SerialName("timezone_offset")
    val timezoneOffset: Int = 0
)