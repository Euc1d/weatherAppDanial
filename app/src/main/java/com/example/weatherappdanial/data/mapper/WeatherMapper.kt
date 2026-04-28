package com.example.weatherappdanial.data.mapper

import com.example.weatherappdanial.data.local.entity.DailyForecastEntity
import com.example.weatherappdanial.data.local.entity.ForecastDetailsEntity
import com.example.weatherappdanial.data.local.entity.HourlyForecastEntity
import com.example.weatherappdanial.data.local.entity.WeatherEntity
import com.example.weatherappdanial.data.local.entity.WeatherWithRelations
import com.example.weatherappdanial.data.local.entity.WindInfoEmbedded
import com.example.weatherappdanial.data.remote.dto.Daily
import com.example.weatherappdanial.data.remote.dto.Hourly
import com.example.weatherappdanial.data.remote.dto.WeatherResponseDTO
import com.example.weatherappdanial.domain.weather_model.DailyForeCast
import com.example.weatherappdanial.domain.weather_model.ForeCastDetails
import com.example.weatherappdanial.domain.weather_model.HourlyForeCast
import com.example.weatherappdanial.domain.weather_model.TodayData
import com.example.weatherappdanial.domain.weather_model.WeatherData
import com.example.weatherappdanial.domain.weather_model.WindInfo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt


fun WeatherResponseDTO.toWeatherEntity(cityName: String): WeatherEntity {
    val today = daily.firstOrNull()
    return WeatherEntity(
        cityName = cityName,
        currentTemp = current.temp.roundToInt(),
        maxTemp = today?.temp?.max?.roundToInt() ?: 0,
        minTemp = today?.temp?.min?.roundToInt() ?: 0,
        todayDescription = current.weather.firstOrNull()?.description.orEmpty()
    )
}

fun WeatherResponseDTO.toDetailsEntity(cityName: String): ForecastDetailsEntity =
    ForecastDetailsEntity(
        cityName = cityName,
        windInfo = WindInfoEmbedded(
            speedKmh = (current.windSpeed * 3.6).roundToInt(),
            gustKmh = (current.windGust * 3.6).roundToInt(),
            directionDeg = current.windDeg
        ),
        sunrise = current.sunrise.toLong(),
        sunset = current.sunset.toLong(),
        humidity = current.humidity,
        pressure = current.pressure,
        dewPoint = current.dewPoint,
        feelsLike = current.feelsLike,
        uvIndex = current.uvi.roundToInt(),
        visibility = current.visibility
    )
fun Hourly.toHourlyEntity(cityName: String): HourlyForecastEntity =
    HourlyForecastEntity(
        timeStamp = dt.toLong(),
        cityName = cityName,
        description = weather.firstOrNull()?.description.orEmpty(),
        temperature = temp.roundToInt(),
        icon = weather.firstOrNull()?.icon.orEmpty()
    )

fun Daily.toDailyEntity(cityName: String): DailyForecastEntity {
    val dayLabel = SimpleDateFormat("EEE", Locale.getDefault())
        .format(Calendar.getInstance().apply { timeInMillis = dt.toLong() * 1000 }.time)
        .replaceFirstChar { it.uppercase() }
    return DailyForecastEntity(
        dayOfWeek = "${cityName}_${dt}",
        cityName = cityName,
        icon = weather.firstOrNull()?.icon.orEmpty(),
        minTemp = "${temp.min.roundToInt()}°",
        maxTemp = "${temp.max.roundToInt()}°"
    )
}


fun WeatherWithRelations.toDomain(): WeatherData = WeatherData(
    todayData = TodayData(
        cityName = weather.cityName,
        currentTemp = weather.currentTemp,
        maxTemp = weather.maxTemp,
        minTemp = weather.minTemp,
        todayDescription = weather.todayDescription
    ),
    hourlyForeCast = hourlyForecast
        .sortedBy { it.timeStamp }
        .map { it.toDomain() },
    dailyForeCast = dailyForecast.map { it.toDomain() },
    weatherDetail = forecastDetails?.toDomain() ?: ForeCastDetails(
        windInfo = WindInfo(0, 0, 0),
        sunrise = 0L, sunset = 0L,
        humidity = 0, pressure = 0,
        dewPoint = 0.0, feelsLike = 0.0,
        UvIndex = 0, visibility = 0
    )
)

private fun HourlyForecastEntity.toDomain(): HourlyForeCast = HourlyForeCast(
    description = description,
    timeStamp = timeStamp,
    temperature = temperature,
    icon = icon
)

private fun DailyForecastEntity.toDomain(): DailyForeCast = DailyForeCast(
    dayOfWeek = dayOfWeek.substringAfterLast("_").let { dt ->
        // Восстанавливаем читаемый день из dt
        SimpleDateFormat("EEE", Locale("ru"))
            .format(Calendar.getInstance().apply { timeInMillis = dt.toLong() * 1000 }.time)
            .replaceFirstChar { it.uppercase() }
    },
    icon = icon,
    minTemp = minTemp,
    maxTemp = maxTemp
)

private fun ForecastDetailsEntity.toDomain(): ForeCastDetails = ForeCastDetails(
    windInfo = WindInfo(
        speedKmh = windInfo.speedKmh,
        gustKmh = windInfo.gustKmh,
        directionDeg = windInfo.directionDeg
    ),
    sunrise = sunrise,
    sunset = sunset,
    humidity = humidity,
    pressure = pressure,
    dewPoint = dewPoint,
    feelsLike = feelsLike,
    UvIndex = uvIndex,
    visibility = visibility
)