package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class PrimaryTypography(
    val screenTitle: TextStyle,
    val cardTextStyle: TextStyle,
    val cardWeatherStyle: TextStyle
)

val weatherTypography = PrimaryTypography(
    cardTextStyle = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = (14 * 1.4).sp,
        letterSpacing = 0.1.sp
    ),
    screenTitle = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Thin,
        fontSize = 14.sp,
        lineHeight = (14 * 1.4).sp,
        letterSpacing = 0.1.sp
    ),
    cardWeatherStyle = TODO(),
)
/* Other default text styles to override
titleLarge = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.sp
),

labelSmall = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)
*/