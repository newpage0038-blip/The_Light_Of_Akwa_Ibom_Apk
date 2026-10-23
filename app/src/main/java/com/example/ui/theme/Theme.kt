package com.example.ui.theme

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

private val LightColorScheme = lightColorScheme(
  primary = AkwaGreenPrimary,
  onPrimary = AkwaWhite,
  primaryContainer = AkwaGreenLight,
  onPrimaryContainer = AkwaGreenDark,

  secondary = RadiantSunOrange,
  onSecondary = AkwaWhite,
  secondaryContainer = RadiantSunLight,
  onSecondaryContainer = Color(0xFF4E1600),

  tertiary = RadiantSunAmber,
  onTertiary = AkwaWhite,
  tertiaryContainer = Color(0xFFFEF3C7),
  onTertiaryContainer = Color(0xFF78350F),

  background = AkwaWhite,
  onBackground = AkwaTextPrimary,

  surface = AkwaSurface,
  onSurface = AkwaTextPrimary,
  surfaceVariant = AkwaSurfaceVariant,
  onSurfaceVariant = AkwaTextSecondary,

  outline = AkwaBorder,
  error = AkwaError,
  onError = AkwaWhite
)

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF4ADE80),
  onPrimary = Color(0xFF003919),
  primaryContainer = AkwaGreenDark,
  onPrimaryContainer = Color(0xFFA7F3D0),

  secondary = Color(0xFFFFB782),
  onSecondary = Color(0xFF4F2500),
  secondaryContainer = Color(0xFF713700),
  onSecondaryContainer = Color(0xFFFFDCC5),

  tertiary = Color(0xFFFBBF24),
  onTertiary = Color(0xFF451A03),

  background = Color(0xFF111412),
  onBackground = Color(0xFFE1E3DF),
  surface = Color(0xFF1A1C1A),
  onSurface = Color(0xFFE1E3DF)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Set dynamicColor to false by default to showcase the Akwa Ibom State colors & identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
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
