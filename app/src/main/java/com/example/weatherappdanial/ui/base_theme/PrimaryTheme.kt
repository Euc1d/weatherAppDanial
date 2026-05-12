package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalWeatherColors     = staticCompositionLocalOf { weatherColors }
val LocalWeatherTypography = staticCompositionLocalOf { weatherTypography }
val LocalWeatherShapes     = staticCompositionLocalOf { weatherShapes }

object PrimaryTheme {
    val colors: PrimaryColors
        @Composable get() = LocalWeatherColors.current
    val typography: PrimaryTypography
        @Composable get() = LocalWeatherTypography.current
    val shapes: PrimaryShapes
        @Composable get() = LocalWeatherShapes.current
}

@Composable
fun PrimaryTheme(
    colors: PrimaryColors         = weatherColors,
    typography: PrimaryTypography = weatherTypography,
    shapes: PrimaryShapes         = weatherShapes,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalWeatherColors     provides colors,
        LocalWeatherTypography provides typography,
        LocalWeatherShapes     provides shapes,
        content                = content
    )
}