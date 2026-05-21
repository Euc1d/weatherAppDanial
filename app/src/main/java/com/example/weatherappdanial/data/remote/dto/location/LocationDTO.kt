package com.example.weatherappdanial.data.remote.dto.location

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDTO(
    val lat: String,
    val lon: String,
    @SerialName("display_name")
    val display_name: String
)