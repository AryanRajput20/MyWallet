package com.example.mywallet.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp

// Colors
val DeepCharcoal = Color(0xFF0F1114)
val LightSlate = Color(0xFF1F2A37)
val ElectricBlue = Color(0xFF0077FF)
val OffWhite = Color(0xFFECEFF1)
val PositiveGreen = Color(0xFF4CAF50)
val NegativeRed = Color(0xFFEF5350)

// Gradient brush used across screens
val GradientBrush = Brush.verticalGradient(
    colors = listOf(DeepCharcoal, LightSlate),
    tileMode = TileMode.Clamp
)

private val DarkColors = darkColorScheme(
    primary = ElectricBlue,
    secondary = OffWhite,
    background = DeepCharcoal,
    surface = LightSlate,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = OffWhite,
    onSurface = OffWhite
)

private val AppTypography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    titleLarge = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 20.sp)
)

@Composable
fun MyWalletTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = AppTypography,
        content = content
    )
}


