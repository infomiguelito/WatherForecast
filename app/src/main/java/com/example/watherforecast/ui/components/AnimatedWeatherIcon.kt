package com.example.watherforecast.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.watherforecast.R

@Composable
fun AnimatedWeatherIcon(
    precipitationProbability: Int,
    modifier: Modifier = Modifier
) {
    val iconRes = when {
        precipitationProbability > 70 -> R.drawable.ic_rain
        precipitationProbability > 30 -> R.drawable.ic_cloud
        else -> R.drawable.ic_sun
    }

    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = "Ícone do tempo",
        tint = Color.White,
        modifier = modifier.size(40.dp)
    )
} 