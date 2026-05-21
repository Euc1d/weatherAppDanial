package com.example.weatherappdanial.data.remote.api

import com.example.weatherappdanial.data.remote.dto.location.LocationDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApiService {
    @GET("search")
    suspend fun getCoordinates(
        @Query("q") city: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 8
    ): List<LocationDTO>

}