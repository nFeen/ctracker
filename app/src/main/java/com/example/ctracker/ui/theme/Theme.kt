package com.example.ctracker.ui.theme

import android.app.Activity
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
    primary = Color(0xFF1B5E20), // Тёмно-зелёный
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4E4E4E), // Нейтральный серый для контейнера
    onPrimaryContainer = Color.White, // Текст или элементы на primaryContainer
    secondary = Color(0xFF6D6D6D), // Нейтральный тёмно-серый для secondary
    tertiary = Color(0xFF6C6C6C), // Приглушённый коралловый
    tertiaryContainer = Color(0xFF6C6C6C), // Тёмно-коричневатый для контейнера
    background = Color(0xFF1A1A1A), // Более светлый чёрный (угольный)
    surface = Color(0xFF2A2A2A), // Чуть более светлая поверхность
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onTertiaryContainer = Color.White, // Светлый текст на тёмном контейнере
    onBackground = Color.White,
    onSurface = Color.White,
    inversePrimary = Color(0xFFFFCC80) // Без изменений
)



private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF66BB6A), // Основной зелёный
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFD0D0D0), // Нейтральный светло-серый
    onPrimaryContainer = Color.Black, // Текст или элементы на primaryContainer
    secondary = Color(0xFFB0BEC5),
    secondaryContainer = Color(0xFFB0BEC5),
    tertiary = Color(0xFFBDBDBD), // Приглушённый светло-серый для tertiary
    tertiaryContainer = Color(0xFFE0E0E0), // Светлый нейтральный серый для контейнера
    background = Color(0xFFE2E2E2), // Светлый фон
    surface = Color(0xFFFFFFFF), // Белая поверхность
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onTertiaryContainer = Color.Black, // Чёрный текст на светлом контейнере
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