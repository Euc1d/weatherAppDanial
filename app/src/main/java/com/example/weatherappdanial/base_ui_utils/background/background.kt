package com.example.weatherappdanial.base_ui_utils.background

import androidx.annotation.DrawableRes
import com.example.weatherappdanial.R
import java.util.Calendar

enum class WeatherBackground {
    MORNING_CLEAR, DAY_CLEAR, EVENING_CLEAR, NIGHT_CLEAR,
    MORNING_RAIN,  DAY_RAIN,  EVENING_RAIN,  NIGHT_RAIN
}

fun resolveBackground(isRaining: Boolean): WeatherBackground {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val slot = when (hour) {
        in 6..11  -> 0   // morning
        in 12..17 -> 1   // day
        in 18..23 -> 2   // evening
        else      -> 3   // night 0-5
    }
    return if (isRaining) {
        arrayOf(
            WeatherBackground.MORNING_RAIN,
            WeatherBackground.DAY_RAIN,
            WeatherBackground.EVENING_RAIN,
            WeatherBackground.NIGHT_RAIN
        )[slot]
    } else {
        arrayOf(
            WeatherBackground.MORNING_CLEAR,
            WeatherBackground.DAY_CLEAR,
            WeatherBackground.EVENING_CLEAR,
            WeatherBackground.NIGHT_CLEAR
        )[slot]
    }
}

@DrawableRes
fun WeatherBackground.toDrawableRes(): Int = when (this) {
    WeatherBackground.MORNING_CLEAR -> R.drawable.ic_background_n_6
    WeatherBackground.DAY_CLEAR     -> R.drawable.ic_background_n_12
    WeatherBackground.EVENING_CLEAR -> R.drawable.ic_background_n_18
    WeatherBackground.NIGHT_CLEAR   -> R.drawable.ic_background_n_24
    WeatherBackground.MORNING_RAIN  -> R.drawable.ic_background_rain_6
    WeatherBackground.DAY_RAIN      -> R.drawable.ic_background_rain_12
    WeatherBackground.EVENING_RAIN  -> R.drawable.ic_background_rain_18
    WeatherBackground.NIGHT_RAIN    -> R.drawable.ic_background_rain_24
}

fun String.isRaining(): Boolean {
    val s = lowercase()
    return listOf("дождь", "ливень", "гроза", "морось", "снег").any { it in s }
}