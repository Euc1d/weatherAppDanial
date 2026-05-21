package com.example.weatherappdanial.ui.base_theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class PrimaryShapes(
    val detailCardShape: Shape,
    val hourlyItemShape: Shape,
    val forecastContainerShape: Shape,
    val bottomSheetShape: Shape,
    val cardShape: Shape,
    val buttonsShape: Shape,
    val searchBarShape: Shape,
)

val weatherShapes = PrimaryShapes(
    detailCardShape        = RoundedCornerShape(16.dp),
    hourlyItemShape        = RoundedCornerShape(40.dp),
    forecastContainerShape = RoundedCornerShape(16.dp),
    bottomSheetShape       = RoundedCornerShape(28.dp),
    cardShape              = RoundedCornerShape(16.dp),
    buttonsShape           = CircleShape,
    searchBarShape         = RoundedCornerShape(40.dp),
)