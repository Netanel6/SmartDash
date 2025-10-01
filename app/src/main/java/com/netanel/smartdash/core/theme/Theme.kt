package com.netanel.smartdash.core.theme

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
    primary = OceanBlue,
    onPrimary = Color.Black,
    primaryContainer = DeepIndigo,
    onPrimaryContainer = Color.White,
    secondary = AquaTeal,
    onSecondary = Color.Black,
    secondaryContainer = DeepIndigo,
    onSecondaryContainer = Color.White,
    tertiary = GoldenSun,
    onTertiary = Color.Black,
    background = SoftSlate,
    onBackground = Color.White,
    surface = SoftSlate,
    onSurface = Color.White,
    surfaceVariant = SlateInk,
    onSurfaceVariant = MistBlue,
    error = CoralError,
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    onPrimary = Color.White,
    primaryContainer = MistBlue,
    onPrimaryContainer = DeepIndigo,
    secondary = AquaTeal,
    onSecondary = Color.White,
    secondaryContainer = AquaTeal.copy(alpha = 0.12f),
    onSecondaryContainer = AquaTeal,
    tertiary = GoldenSun,
    onTertiary = Color.Black,
    background = CloudGray,
    onBackground = SlateInk,
    surface = Color.White,
    onSurface = SlateInk,
    surfaceVariant = MistBlue,
    onSurfaceVariant = DeepIndigo,
    error = CoralError,
    onError = Color.White
)

@Composable
fun SmartDashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We keep dynamic colors opt-in so the custom palette stays consistent.
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