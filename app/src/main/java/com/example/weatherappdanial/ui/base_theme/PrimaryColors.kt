package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val white200 = Color(0xfffcfffb)
val white100 = Color(0x85ffffff)

val light_blue100 = Color(0xFFC9FFFA)
val blue_200 = Color(0xFF377DC3)
val blue_300 = Color(0xFF3916E9)
val gray_300 = Color(0x85424d58)
@Immutable
data class PrimaryColors (
    val backgroundDark: Color,
    val backgroundMedium: Color,
    val backgroundLight: Color,
    val drawer: Color,
)

val weatherColors = PrimaryColors(
    backgroundDark   = blue_300,
    backgroundMedium = blue_200,  // карточки городов
    backgroundLight  =  white100,  // поисковая строка
    drawer           = gray_300
)