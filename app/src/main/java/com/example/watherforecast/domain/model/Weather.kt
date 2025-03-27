package com.example.watherforecast.domain.model

data class Weather(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val humidity: Int,
    val windSpeed: Double,
    val precipitationProbability: Int
) 