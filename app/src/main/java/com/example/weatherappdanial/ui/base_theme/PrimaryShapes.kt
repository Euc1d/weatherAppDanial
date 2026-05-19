package com.example.weatherappdanial.ui.base_theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class PrimaryShapes(
    // Wide 2-column detail cards (ForecastDetailsEntity rows)
    val detailCardShape: Shape,
    // Hourly strip item pills (HourlyForecastEntity)
    val hourlyItemShape: Shape,
    // 10-day forecast row container (DailyForecastEntity)
    // — typically no rounding needed, whole list has one container shape
    val forecastContainerShape: Shape,
    // Bottom sheet / drawer
    val bottomSheetShape: Shape,
    // General card fallback
    val cardShape: Shape,
    // Buttons / search bar
    val buttonsShape: Shape,
    val searchBarShape: Shape,
)

val weatherShapes = PrimaryShapes(
    detailCardShape        = RoundedCornerShape(16.dp), // wide grid cards
    hourlyItemShape        = RoundedCornerShape(40.dp), // pill-shaped hourly items
    forecastContainerShape = RoundedCornerShape(16.dp), // 10-day list container
    bottomSheetShape       = RoundedCornerShape(28.dp),
    cardShape              = RoundedCornerShape(16.dp),
    buttonsShape           = RoundedCornerShape(40.dp),
    searchBarShape         = RoundedCornerShape(40.dp),
)