package com.example.weatherappdanial.base_ui_utils.formatter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.weatherappdanial.R
import com.example.weatherappdanial.domain.weather_model.ForeCastDetails
import com.example.weatherappdanial.domain.weather_model.HourlyForeCast
import com.example.weatherappdanial.domain.weather_model.TodayData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt


fun Long.toLastUpdated(context: Context): String {
    val diff = System.currentTimeMillis() - this
    return when {
        diff < 60_000L    -> context.getString(R.string.last_updated_just_now)
        diff < 3_600_000L -> context.getString(R.string.last_updated_minutes_ago, diff / 60_000)
        else -> context.getString(
            R.string.last_updated_at,
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this))
        )
    }
}

fun Long.formatAsTime(): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(this * 1000))

fun Long.formatAsHour(): String =
    SimpleDateFormat("HH", Locale.getDefault()).format(Date(this * 1000))


fun Int.toWindDir(context: Context): String {
    val dirs = context.resources.getStringArray(R.array.wind_directions)
    return dirs[((this + 22) / 45) % 8]
}

@StringRes
fun Int.toUvDescriptionRes(): Int = when (this) {
    in 0..2  -> R.string.uv_low
    in 3..5  -> R.string.uv_moderate
    in 6..7  -> R.string.uv_high
    in 8..10 -> R.string.uv_very_high
    else     -> R.string.uv_extreme
}


data class DetailCardData(
    @StringRes val label: Int,
    val mainValue: String,
    @StringRes val subtext: Int? = null,
    val subtextArgs: List<Any> = emptyList(),
    @DrawableRes val icon: Int
)

fun DetailCardData.resolveSubtext(context: Context): String? =
    subtext?.let { context.getString(it, *subtextArgs.toTypedArray()) }


fun ForeCastDetails.toDetailCards(): List<DetailCardData> = listOf(
    DetailCardData(
        label     = R.string.uv_index,
        mainValue = "$UvIndex",
        subtext   = UvIndex.toUvDescriptionRes(),
        icon      = R.drawable.ic_sun
    ),
    DetailCardData(
        label     = R.string.sunset,
        mainValue = sunset.formatAsTime(),
        subtext   = R.string.sunrise_in,
        subtextArgs = listOf(sunrise.formatAsTime()),
        icon      = R.drawable.ic_sunset
    ),
    DetailCardData(
        label     = R.string.humidity,
        mainValue = "$humidity%",
        subtext   = R.string.dew_point,
        subtextArgs = listOf(dewPoint.roundToInt()),
        icon      = R.drawable.ic_humidity
    ),
    DetailCardData(
        label     = R.string.pressure,
        mainValue = "$pressure",
        subtext   = R.string.hpa,
        icon      = R.drawable.ic_squeeze
    ),
    DetailCardData(
        label     = R.string.feels_like,
        mainValue = "${feelsLike.roundToInt()}°",
        subtext   = if (feelsLike > 0) R.string.warmer_than_actual
        else               R.string.colder_than_actual,
        icon      = R.drawable.ic_thermometer
    ),
)

sealed interface HourlyDisplayItem {
    data class Forecast(val data: HourlyForeCast, val isNow: Boolean = false) : HourlyDisplayItem
    data class SunEvent(val isSunset: Boolean, val timeUnix: Long) : HourlyDisplayItem
}

fun buildHourlyItems(
    hourly  : List<HourlyForeCast>,
    sunrise : Long,
    sunset  : Long
): List<HourlyDisplayItem> {
    val result: MutableList<HourlyDisplayItem> = hourly
        .take(24)
        .mapIndexed {index, item -> HourlyDisplayItem.Forecast(item, index == 0) }
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

fun TodayData.toAvgCard(): DetailCardData = DetailCardData(
    label     = R.string.avg_label,
    mainValue = if (diffFromAvgMax >= 0) "На ${diffFromAvgMax}°" else "На ${-diffFromAvgMax}°",
    subtext   = if (diffFromAvgMax > 0) R.string.above_avg_max
    else if (diffFromAvgMax < 0) R.string.below_avg_max
    else R.string.equal_avg_max,
    icon      = R.drawable.ic_graph
)