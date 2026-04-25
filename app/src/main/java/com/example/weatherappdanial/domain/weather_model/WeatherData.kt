package com.example.weatherappdanial.domain.weather_model

data class WeatherData(
    val todayData: TodayData,
    val hourlyForeCast: List<HourlyForeCast>,
    val dailyForeCast: List<DailyForeCast>,
    val weatherDetail: ForeCastDetails
)