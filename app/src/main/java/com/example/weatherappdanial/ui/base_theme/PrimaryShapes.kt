package com.example.weatherappdanial.ui.base_theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class PrimaryShapes(
    val cardShape: Shape,
    val buttonsShape: Shape,
    val searchBarShape: Shape,
    val bottomSheetShape: Shape
)
val weatherShapes = PrimaryShapes(
    cardShape = RoundedCornerShape(12.dp),
    buttonsShape = RoundedCornerShape(40.dp),
    searchBarShape = RoundedCornerShape(40.dp),
    bottomSheetShape = RoundedCornerShape(28.dp)
)