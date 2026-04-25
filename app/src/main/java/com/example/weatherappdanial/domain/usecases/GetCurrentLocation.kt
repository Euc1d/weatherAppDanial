package com.example.weatherappdanial.domain.usecases

import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.repository.LocationRepository
import com.example.weatherappdanial.domain.result.LocationError
import com.example.weatherappdanial.domain.result.Result

class GetCurrentLocation(private val repository: LocationRepository) {
    suspend operator fun invoke(): Result<Location, LocationError> {
        return repository.getCurrentLocation()
    }

}