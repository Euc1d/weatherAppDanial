package com.example.weatherappdanial.domain.result

enum class LocationError: Error {
    NO_PERMISSION,
    GPS_DISABLED,
    UNKNOWN
}