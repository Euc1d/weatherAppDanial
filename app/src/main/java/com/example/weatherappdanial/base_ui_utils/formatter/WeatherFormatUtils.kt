package com.example.weatherappdanial.base_ui_utils.formatter

import com.example.weatherappdanial.domain.weather_model.ForeCastDetails
import com.example.weatherappdanial.domain.weather_model.HourlyForeCast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


fun Long.toLastUpdated(): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000L    -> "Только что"
        diff < 3_600_000L -> "Обновлено ${diff / 60_000} мин. назад"
        else -> "Обновлено в ${
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
        }"
    }
}
fun Int.toWindDir(): String =
    listOf("С","СВ","В","ЮВ","Ю","ЮЗ","З","СЗ")[((this + 22) / 45) % 8]
fun Long.formatAsTime(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this * 1000))

fun Long.formatAsHour(): String =
    SimpleDateFormat("HH", Locale.getDefault()).format(Date(this * 1000))


fun Int.toWindDirection(): String {
    val dirs = listOf("С", "СВ", "В", "ЮВ", "Ю", "ЮЗ", "З", "СЗ")
    return dirs[((this + 22) / 45) % 8]
}


fun Int.toUvDescription(): String = when (this) {
    in 0..2  -> "Низкий"
    in 3..5  -> "Умеренный"
    in 6..7  -> "Высокий"
    in 8..10 -> "Очень высокий"
    else     -> "Экстремальный"
}


data class DetailCardData(
    val label: String,
    val mainValue: String,
    val subtext: String? = null
)

fun ForeCastDetails.toDetailCards(): List<DetailCardData> = listOf(
    DetailCardData(
        label     = "Ветер",
        mainValue = "${windInfo.speedKmh} км/ч",
        subtext   = "Порывы: ${windInfo.gustKmh} км/ч\n" +
                "Направление: ${windInfo.directionDeg}° ${windInfo.directionDeg.toWindDirection()}"
    ),
    DetailCardData(
        label     = "УФ-индекс",
        mainValue = "$UvIndex",
        subtext   = UvIndex.toUvDescription()
    ),
    DetailCardData(
        label     = "Закат",
        mainValue = sunset.formatAsTime(),
        subtext   = "Восход в ${sunrise.formatAsTime()}"
    ),
    DetailCardData(
        label     = "Влажность",
        mainValue = "$humidity%",
        subtext   = "Точка росы сейчас: ${dewPoint.roundToInt()}°"
    ),
    DetailCardData(
        label     = "Давление",
        mainValue = "$pressure",
        subtext   = "гПА"
    ),
    DetailCardData(
        label     = "Ощущается как",
        mainValue = "${feelsLike.roundToInt()}°",
        subtext   = if (feelsLike > 0) "По ощущениям теплее, чем на самом деле."
        else "По ощущениям холоднее, чем на самом деле."
    ),
)


sealed interface HourlyDisplayItem {
    data class Forecast(val data: HourlyForeCast) : HourlyDisplayItem
    data class SunEvent(val isSunset: Boolean, val timeUnix: Long) : HourlyDisplayItem
}

fun buildHourlyItems(
    hourly: List<HourlyForeCast>,
    sunrise: Long,
    sunset: Long
): List<HourlyDisplayItem> {
    val result: MutableList<HourlyDisplayItem> = hourly
        .take(24)
        .map { HourlyDisplayItem.Forecast(it) }
        .toMutableList()

    fun insert(event: HourlyDisplayItem.SunEvent) {
        val idx = result.indexOfFirst { item ->
            item is HourlyDisplayItem.Forecast && item.data.timeStamp > event.timeUnix
        }
        if (idx >= 0) result.add(idx, event)
    }

    insert(HourlyDisplayItem.SunEvent(isSunset = false, timeUnix = sunrise))
    insert(HourlyDisplayItem.SunEvent(isSunset = true,  timeUnix = sunset))
    return result
}

