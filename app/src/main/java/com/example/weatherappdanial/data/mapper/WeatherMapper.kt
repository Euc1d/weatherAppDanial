package com.example.weatherappdanial.data.mapper

import com.example.weatherappdanial.data.local.entity.weather.DailyForecastEntity
import com.example.weatherappdanial.data.local.entity.weather.ForecastDetailsEntity
import com.example.weatherappdanial.data.local.entity.weather.HourlyForecastEntity
import com.example.weatherappdanial.data.local.entity.weather.WeatherEntity
import com.example.weatherappdanial.data.local.entity.weather.WeatherWithRelations
import com.example.weatherappdanial.data.local.entity.weather.WindInfoEmbedded
import com.example.weatherappdanial.data.remote.dto.onecall.Daily
import com.example.weatherappdanial.data.remote.dto.onecall.Hourly
import com.example.weatherappdanial.data.remote.dto.onecall.WeatherResponseDTO
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
        todayDescription = current.weather.firstOrNull()?.description.orEmpty(),
        timezoneOffsetSec = timezoneOffset
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
        maxTemp = "${temp.max.roundToInt()}°",
        summary = summary
    )
}


fun WeatherWithRelations.toDomain(): WeatherData {
    val maxTemps = dailyForecast.mapNotNull {
        it.maxTemp.dropLast(1).toIntOrNull()
    }
    val avgMax     = if (maxTemps.isNotEmpty()) maxTemps.average().roundToInt() else 0
    val todayMax   = weather.maxTemp
    val diff       = todayMax - avgMax

    return WeatherData(
        todayData = TodayData(
            cityName         = weather.cityName,
            currentTemp      = weather.currentTemp,
            maxTemp          = todayMax,
            minTemp          = weather.minTemp,
            todayDescription = weather.todayDescription,
            avgMaxTemp       = avgMax,
            diffFromAvgMax   = diff,
            timezoneOffsetSec = weather.timezoneOffsetSec
        ),
        hourlyForeCast = hourlyForecast.sortedBy { it.timeStamp }.map { it.toDomain() },
        dailyForeCast  = dailyForecast
            .sortedBy { it.dayOfWeek.substringAfterLast("_").toLongOrNull() ?: 0L }
            .mapIndexed { i, e -> e.toDomain(isToday = i == 0) },
        weatherDetail  = forecastDetails?.toDomain() ?: ForeCastDetails(
            windInfo = WindInfo(0, 0, 0),
            sunrise = 0L, sunset = 0L,
            humidity = 0, pressure = 0,
            dewPoint = 0.0, feelsLike = 0.0,
            UvIndex = 0, visibility = 0
        ),
        cachedAt = weather.cachedAt
    )
}

private fun DailyForecastEntity.toDomain(isToday: Boolean = false): DailyForeCast = DailyForeCast(
    dayOfWeek = if (isToday) "Сегодня" else
        dayOfWeek.substringAfterLast("_").let { dt ->
            SimpleDateFormat("EEE", Locale("ru"))
                .format(Calendar.getInstance().apply { timeInMillis = dt.toLong() * 1000 }.time)
                .replaceFirstChar { it.uppercase() }
        },
    icon    = icon,
    minTemp = minTemp,
    maxTemp = maxTemp,
    summary = summary
)

private fun HourlyForecastEntity.toDomain(): HourlyForeCast = HourlyForeCast(
    description = description,
    timeStamp = timeStamp,
    temperature = temperature,
    icon = icon
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
