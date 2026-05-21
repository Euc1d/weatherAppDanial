package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class PrimaryTypography(


    val cityTitle: TextStyle,
    val temperatureDisplay: TextStyle,

    val conditionLabel: TextStyle,

    val minMaxLabel: TextStyle,


    val hourlyTime: TextStyle,
    val hourlyTemp: TextStyle,


    val forecastDayName: TextStyle,
    val forecastTempRange: TextStyle,

    val detailCardLabel: TextStyle,
    val detailCardValue: TextStyle,
    val detailCardSubtext: TextStyle,

    val cardTextStyle: TextStyle,
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