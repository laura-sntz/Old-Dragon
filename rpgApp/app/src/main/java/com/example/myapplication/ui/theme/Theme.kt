package com.example.myapplication.ui.theme

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

// Tema escuro - Taverna Medieval
private val MedievalDarkColorScheme = darkColorScheme(
    primary = TavernGold,
    secondary = TavernWood,
    tertiary = MedievalCream,
    background = TavernDark,
    surface = MedievalDarkBrown,
    onPrimary = TavernDark,
    onSecondary = MedievalCream,
    onTertiary = TavernDark,
    onBackground = MedievalCream,
    onSurface = MedievalCream
)

// Tema claro - Campo Medieval
private val MedievalLightColorScheme = lightColorScheme(
    primary = MedievalDarkBrown,
    secondary = MedievalBrown,
    tertiary = MedievalRed,
    background = FieldLight,
    surface = MedievalCream,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = MedievalDarkBrown,
    onSurface = MedievalDarkBrown
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desabilitando dynamic color para manter o tema medieval
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> MedievalDarkColorScheme
        else -> MedievalLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

