package com.hanirweshanimeking.mobilejarvis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MobileJarvisColorScheme = darkColorScheme(
    primary = Color(0xFF62F2FF),
    secondary = Color(0xFF8B5CF6),
    tertiary = Color(0xFF56F0A8),
    background = Color(0xFF070B14),
    surface = Color(0xFF101A2B),
    onPrimary = Color(0xFF06131D),
    onSecondary = Color(0xFFEAF7FF),
    onBackground = Color(0xFFEAF7FF),
    onSurface = Color(0xFFEAF7FF),
    error = Color(0xFFFF6B75),
    onError = Color(0xFF210A10)
)

@Composable
fun MobileJarvisTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MobileJarvisColorScheme,
        content = content
    )
}
