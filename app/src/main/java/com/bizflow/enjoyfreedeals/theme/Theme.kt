package com.bizflow.enjoyfreedeals.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryRed,
    secondary = PrimaryGreen,
    tertiary = AccentYellow,
    background = AppBackground,
    surface = CardWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark
)

private val DarkColors = darkColorScheme(
    primary = PrimaryRed,
    secondary = PrimaryGreen,
    tertiary = AccentYellow,
    background = Color(0xFF101211),
    surface = Color(0xFF1A1D1B),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun EnjoyFreeDealsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
}
