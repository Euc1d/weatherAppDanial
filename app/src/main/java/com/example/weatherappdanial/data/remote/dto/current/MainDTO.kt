package com.example.weatherappdanial.data.remote.dto.current

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MainDTO(
    @SerialName("temp")     val temp    : Double = 0.0,
    @SerialName("temp_min") val tempMin : Double = 0.0,
    @SerialName("temp_max") val tempMax : Double = 0.0,
)