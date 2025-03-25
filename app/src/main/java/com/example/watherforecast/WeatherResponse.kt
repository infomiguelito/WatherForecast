package com.example.watherforecast


data class WeatherResponse(
    val result: List<WeatherData>
)

data class WeatherData(
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val date: String,
    val maxTemperature: Double,
    val minTemperature: Double,
    val precipitationProbability: Int
)

