package com.example.weatherappdanial.ui.base_theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// ─── Raw palette ──────────────────────────────────────────────
// Backgrounds (deep navy gradient, top -> bottom)
val navy_900        = Color(0xFF142236)   // darkest top
val navy_700        = Color(0xFF1E3A56)   // mid gradient
val navy_500        = Color(0xFF2C5480)   // lightest bg accent

// Glass card surface (semi-transparent, sits on navy bg)
val card_glass      = Color(0x4D1E3A56)   // ~30% navy for wide detail cards
val card_glass_lite = Color(0x33FFFFFF)   // ~20% white frost for hourly strip

// Text
val white_full      = Color(0xFFFFFFFF)
val white_70        = Color(0xB3FFFFFF)   // secondary text, values on cards
val white_45        = Color(0x73FFFFFF)   // tertiary / card label hint text

// Legacy (kept for backward compat)
val white200        = Color(0xfffcfffb)
val white100        = Color(0x85ffffff)
val light_blue100   = Color(0xFFC9FFFA)
val blue_200        = Color(0xFF377DC3)
val blue_300        = Color(0xFF1E3A56)   // corrected: was 0xFF3916E9 (purple), now proper navy
val gray_300        = Color(0x85424d58)

// ─── Theme contract ───────────────────────────────────────────
@Immutable
data class PrimaryColors(
    // Background gradient layers
    val backgroundDark: Color,    // top of screen / status bar area
    val backgroundMedium: Color,  // mid-page gradient stop
    val backgroundLight: Color,   // used for highlights, active states

    // Card surfaces
    val drawer: Color,            // bottom sheet / side drawer
    val cardGlass: Color,         // wide detail cards + hourly card bg
    val cardGlassFrost: Color,    // hourly strip item bg (lighter frost)

    // Text
    val textPrimary: Color,       // city name, main temp, card values
    val textSecondary: Color,     // condition description, min/max, subtexts
    val textHint: Color,          // small ALL-CAPS labels on detail cards
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
)