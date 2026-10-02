package com.pemmob.smartcanteen.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CanteenPrimary,
    onPrimary = CanteenOnPrimary,
    primaryContainer = CanteenPrimaryContainer,
    onPrimaryContainer = CanteenSecondary,
    secondary = CanteenSecondary,
    onSecondary = CanteenOnPrimary,
    secondaryContainer = CanteenSecondaryContainer,
    background = CanteenWarmBg,
    onBackground = CanteenTextPrimary,
    surface = CanteenSurface,
    onSurface = CanteenTextPrimary,
    surfaceVariant = CanteenSurfaceVariant,
    onSurfaceVariant = CanteenTextSecondary,
    outline = CanteenBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = CanteenPrimary,
    onPrimary = CanteenOnPrimary,
    primaryContainer = CanteenSecondary,
    onPrimaryContainer = CanteenPrimaryContainer,
    secondary = CanteenSecondaryContainer,
    onSecondary = CanteenSecondary,
    background = CanteenTextPrimary,
    onBackground = CanteenWarmBg,
    surface = Color(0xFF2C241E),
    onSurface = CanteenWarmBg
)

@Composable
fun SmartCanteenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}