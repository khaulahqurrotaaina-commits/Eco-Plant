package com.khaulahqurrota.ecoplant.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EcoPlantColors = lightColorScheme(
    primary = ForestGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = LeafGreen,
    background = WarmCream,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = PlantInk,
    tertiary = PlantGold,
)

@Composable
fun EcoPlantTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(colorScheme = EcoPlantColors, typography = Typography, content = content)
}
