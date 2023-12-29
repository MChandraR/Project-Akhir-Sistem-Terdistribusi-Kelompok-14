package com.mcr.mnews.ui.theme

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
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
import com.mcr.mnews.util.ColorPalettes
import com.mcr.mnews.util.util.colorPalettes

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MNewsTheme(
    color: Color = BlueCoral,
    context: Context,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit

) {
    val SP:SharedPreferences = context.getSharedPreferences("mcr",Context.MODE_PRIVATE)

    val DarkColorScheme = darkColorScheme(
        primary = color,
        secondary = ColorPalettes().darkBlues,
        tertiary = Color.White,
        outline = Color.White,
        background = Color.Black,
        inverseSurface = Color.Black,
        surface = ColorPalettes().darkBlues,
        primaryContainer = ColorPalettes().white10,
        surfaceVariant = ColorPalettes().white10
    )

    val LightColorScheme = lightColorScheme(
        primary = color,
        secondary = ColorPalettes().darks10,
        tertiary = color,
        outline = ColorPalettes().darkBlues,
        background = Color.White,
        inverseSurface = Color.White,
        surface = ColorPalettes().darkBlues,
        primaryContainer = ColorPalettes().darks10,
        surfaceVariant = ColorPalettes().darks10


        /* Other default colors to override
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
        */
    )

    val theme:String = SP.getString("theme","")!!
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        theme.equals("light") -> LightColorScheme
        theme.equals("dark") -> DarkColorScheme
        else-> when{
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

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