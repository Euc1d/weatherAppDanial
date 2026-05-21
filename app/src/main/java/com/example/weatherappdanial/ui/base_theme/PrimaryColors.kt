package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val navy_900        = Color(0xFF142236)
val navy_700        = Color(0xFF1E3A56)
val navy_500        = Color(0xFF2C5480)

val card_glass      = Color(0x4D1E3A56)
val card_glass_lite = Color(0x33FFFFFF)

val white_full      = Color(0xFFFFFFFF)
val white_70        = Color(0xB3FFFFFF)
val white_45        = Color(0x73FFFFFF)

val light_blue100   = Color(0xFFC9FFFA)

val gray_300        = Color(0x85424d58)
val white_blue_100        = Color(0xFF5BC8F5)

@Immutable
data class PrimaryColors(
    val backgroundDark: Color,
    val backgroundMedium: Color,
    val backgroundLight: Color,
    val drawer: Color,
    val cardGlass: Color,
    val cardGlassFrost: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textHint: Color,

    val tempIndicator: Color
)

val weatherColors = PrimaryColors(
    backgroundDark    = navy_900,
    backgroundMedium  = navy_700,
    backgroundLight   = navy_500,
    drawer            = gray_300,
    cardGlass         = card_glass,
    cardGlassFrost    = card_glass_lite,
    textPrimary       = white_full,
    textSecondary     = white_70,
    textHint          = white_45,
    tempIndicator = white_blue_100
)