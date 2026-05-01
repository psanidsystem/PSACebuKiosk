package com.example.psacebukiosk.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PsaBlue,
    secondary = PsaGold,
    tertiary = PsaSky,
    background = BackgroundGray,
    surface = PsaSurface,
    onPrimary = PsaSurface,
    onSecondary = PsaNavy,
    onBackground = PsaNavy,
    onSurface = PsaNavy
)

@Composable
fun PSACebuKioskTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
