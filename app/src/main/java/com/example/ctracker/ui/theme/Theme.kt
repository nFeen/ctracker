package com.example.ctracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF66BB6A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4E4E4E),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF6D6D6D),
    tertiary = Color(0xFF888888),
    tertiaryContainer = Color(0xFF6C6C6C),
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2A2A2A),
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onTertiaryContainer = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    inversePrimary = Color(0xFFFFCC80)
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF66BB6A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0D0D0),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFFB0BEC5),
    secondaryContainer = Color(0xFFB0BEC5),
    tertiary = Color(0xFFBDBDBD),
    tertiaryContainer = Color(0xFFE0E0E0),
    background = Color(0xFFE2E2E2),
    surface = Color(0xFFFFFFFF),
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onTertiaryContainer = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    inversePrimary = Color(0xFFFFCC80),
)


@Composable
fun CTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}