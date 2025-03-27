package com.example.watherforecast.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedWeatherIcon(precipitationProbability: Int) {
    val icon = when {
        precipitationProbability > 70 -> Icons.Default.WaterDrop
        precipitationProbability > 30 -> Icons.Default.Cloud
        else -> Icons.Default.WbSunny
    }

    Icon(
        imageVector = icon,
        contentDescription = "Ícone do clima",
        modifier = Modifier.size(24.dp),
        tint = Color.White
    )
} 