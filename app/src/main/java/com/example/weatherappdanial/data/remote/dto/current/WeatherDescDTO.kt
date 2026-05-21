package com.example.weatherappdanial.data.remote.dto.current

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDescDTO(
    @SerialName("description") val description: String = "",
    @SerialName("icon")        val icon       : String = ""
)