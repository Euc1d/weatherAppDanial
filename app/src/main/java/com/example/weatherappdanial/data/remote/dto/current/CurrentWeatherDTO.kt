package com.example.weatherappdanial.data.remote.dto.current

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDTO(
    @SerialName("name")    val name   : String  = "",
    @SerialName("main")    val main   : MainDTO,
    @SerialName("weather") val weather: List<WeatherDescDTO> = emptyList(),
    @SerialName("dt")      val dt     : Long    = 0L,
    @SerialName("timezone") val timezone: Int     = 0
)