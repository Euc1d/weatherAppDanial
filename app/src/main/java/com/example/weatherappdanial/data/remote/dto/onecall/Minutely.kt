package com.example.weatherappdanial.data.remote.dto.onecall


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Minutely(
    @SerialName("dt") val dt: Int = 0,
    @SerialName("precipitation") val precipitation: Double = 0.0
)