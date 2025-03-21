package com.example.watherforecast

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current")
    val current: Current,
    @SerializedName("daily")
    val daily: Daily
)

data class Current(
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("relative_humidity_2m")
    val humidity: Int,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double
)

data class Daily(
    @SerializedName("time")
    val dates: List<String>,
    @SerializedName("temperature_2m_max")
    val maxTemperatures: List<Double>,
    @SerializedName("temperature_2m_min")
    val minTemperatures: List<Double>,
    @SerializedName("precipitation_probability_max")
    val precipitationProbabilities: List<Int>
) 