package com.example.watherforecast

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherScreen(
    weatherData: WeatherResponse?,
    isLoading: Boolean,
    error: String?
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            weatherData != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Dados atuais
                    weatherData.result.firstOrNull()?.let { current ->
                        Text(
                            text = "Temperatura Atual: ${current.temperature}°C",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Umidade: ${current.humidity}%",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Velocidade do Vento: ${current.windSpeed} km/h",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Previsão diária
                    if (weatherData.result.size > 1) {
                        Text(
                            text = "Previsão para os próximos dias:",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        weatherData.result.drop(1).take(5).forEach { forecast ->
                            Text(
                                text = "Data: ${forecast.date}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Máx: ${forecast.maxTemperature}°C, Mín: ${forecast.minTemperature}°C",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Chance de chuva: ${forecast.precipitationProbability}%",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
} 