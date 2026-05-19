package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class PrimaryTypography(

    // ── Top section (WeatherEntity) ──────────────────────────────
    // "Алматы" — large city name just below status bar
    val cityTitle: TextStyle,
    // "11°" — the giant main temperature
    val temperatureDisplay: TextStyle,
    // "В основном солнечно" — one-line condition description
    val conditionLabel: TextStyle,
    // "Макс.: 11°, мин.: 5°" — compact min/max line
    val minMaxLabel: TextStyle,

    // ── Hourly strip (HourlyForecastEntity) ──────────────────────
    // "16", "17", "Закат" — time labels
    val hourlyTime: TextStyle,
    // "10°", "9°" — temperature per hour
    val hourlyTemp: TextStyle,

    // ── 10-day forecast (DailyForecastEntity) ────────────────────
    // "Сегодня", "Сб", "Вс" — day name column
    val forecastDayName: TextStyle,
    // "5°"  "11°" — min/max temperature ends of the range bar
    val forecastTempRange: TextStyle,

    // ── Wide detail cards (ForecastDetailsEntity) ────────────────
    // "ВЕТЕР", "УФ-ИНДЕКС", "ВЛАЖНОСТЬ" — small ALL-CAPS card label
    val detailCardLabel: TextStyle,
    // "6 км/ч", "17:29", "70%", "1 022" — the big value
    val detailCardValue: TextStyle,
    // "Порывы ветра: 14 км/ч", "Остаётся низким до конца дня."
    // — the small descriptive line at the bottom of each card
    val detailCardSubtext: TextStyle,

    // ── Legacy / general (kept for backward compat) ──────────────
    // General card label text (maps to detailCardLabel use-case)
    val cardTextStyle: TextStyle,
    // General screen-level title fallback
    val screenTitle: TextStyle,
)

val weatherTypography = PrimaryTypography(

    cityTitle = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 28.sp,
        lineHeight   = 34.sp,
        letterSpacing = 0.sp,
    ),
    temperatureDisplay = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Thin,
        fontSize     = 80.sp,
        lineHeight   = 80.sp,
        letterSpacing = (-2).sp,
    ),
    conditionLabel = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 16.sp,
        lineHeight   = 22.sp,
        letterSpacing = 0.1.sp,
    ),
    minMaxLabel = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.1.sp,
    ),

    hourlyTime = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Medium,
        fontSize     = 13.sp,
        lineHeight   = 18.sp,
        letterSpacing = 0.2.sp,
    ),
    hourlyTemp = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 15.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.sp,
    ),

    forecastDayName = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Medium,
        fontSize     = 16.sp,
        lineHeight   = 22.sp,
        letterSpacing = 0.sp,
    ),
    forecastTempRange = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Medium,
        fontSize     = 16.sp,
        lineHeight   = 22.sp,
        letterSpacing = 0.sp,
    ),

    detailCardLabel = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.8.sp,
    ),
    detailCardValue = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal ,
        fontSize     = 28.sp,
        lineHeight   = 34.sp,
        letterSpacing = (-0.5).sp,
    ),
    detailCardSubtext = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Normal,
        fontSize     = 13.sp,
        lineHeight   = (13 * 1.4).sp,
        letterSpacing = 0.1.sp,
    ),

    cardTextStyle = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Medium,
        fontSize     = 14.sp,
        lineHeight   = (14 * 1.4).sp,
        letterSpacing = 0.1.sp,
    ),
    screenTitle = TextStyle(
        fontFamily   = FontFamily.Default,
        fontWeight   = FontWeight.Thin,
        fontSize     = 20.sp,
        lineHeight   = 26.sp,
        letterSpacing = 0.1.sp,
    ),
)