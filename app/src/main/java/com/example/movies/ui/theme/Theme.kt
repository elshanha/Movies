package com.example.movies.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF7BC70),
    onPrimary = Color(0xFF462A00),
    secondaryContainer = Color(0xFF57432B),
    onSecondaryContainer = Color(0xFFFCDEBC),
    background = Color(0xFF18120C),
    secondary = Color(0xFFEDE0D4),
    onSecondary = Color(0xFFEDE0D4).copy(alpha = .6f),
    outline = Color(0xFF9C8E80),
    surface = Color(0xFF302921),
    error = Color(0xFFB00020),

    )

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF815512),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFCDEBC),
    onSecondaryContainer = Color(0xFF271905),
    background = Color(0xFFFFF8F4),
    secondary = Color(0xFF211A13),
    onSecondary = Color(0xFF211A13).copy(alpha = .6f),
    outline = Color(0xFF827568),
    surface = Color(0xFFF3E6DA),
    error = Color(0xFFFF002E),
    )

@Composable
fun MoviesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}