package com.wiom.csp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val WiomColorScheme = lightColorScheme(
    primary = WiomPrimary,
    onPrimary = Color.White,
    primaryContainer = WiomPrimaryLight,
    onPrimaryContainer = WiomPrimary,
    secondary = WiomInfo,
    onSecondary = Color.White,
    secondaryContainer = WiomInfo100,
    onSecondaryContainer = WiomInfo,
    tertiary = WiomPositive,
    onTertiary = Color.White,
    tertiaryContainer = WiomPositive100,
    onTertiaryContainer = WiomPositive,
    error = WiomNegative,
    onError = Color.White,
    errorContainer = WiomNegative100,
    onErrorContainer = WiomNegative,
    background = WiomSurface,
    onBackground = WiomText,
    surface = WiomSurface,
    onSurface = WiomText,
    surfaceVariant = WiomBgSec,
    onSurfaceVariant = WiomTextSec,
    outline = WiomBorderInput,
    outlineVariant = WiomBorder,
)

@Composable
fun WiomCspTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WiomColorScheme,
        typography = WiomTypography,
        shapes = WiomShapes,
        content = content
    )
}
