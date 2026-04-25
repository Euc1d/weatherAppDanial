package com.example.weatherappdanial.domain.repository

import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.result.LocationError
import com.example.weatherappdanial.domain.result.Result
import com.example.weatherappdanial.domain.result.RootError

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Location, LocationError>
}