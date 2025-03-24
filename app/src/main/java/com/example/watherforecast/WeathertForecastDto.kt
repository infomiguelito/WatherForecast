package com.example.watherforecast

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("timezone")
    val timezone: String,
    
    @SerializedName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    
    @SerializedName("elevation")
    val elevation: Double,
    
    @SerializedName("current_units")
    val currentUnits: CurrentUnits,
    
    @SerializedName("current")
    val current: CurrentWeather,
    
    @SerializedName("daily_units")
    val dailyUnits: DailyUnits,
    
    @SerializedName("daily")
    val daily: DailyWeather
)

data class CurrentUnits(
    @SerializedName("time")
    val time: String,
    
    @SerializedName("temperature_2m")
    val temperature: String,
    
    @SerializedName("relative_humidity_2m")
    val humidity: String,
    
    @SerializedName("wind_speed_10m")
    val windSpeed: String
)

data class CurrentWeather(
    @SerializedName("time")
    val time: String,
    
    @SerializedName("temperature_2m")
    val temperature: Double,
    
    @SerializedName("relative_humidity_2m")
    val humidity: Int,
    
    @SerializedName("wind_speed_10m")
    val windSpeed: Double
)

data class DailyUnits(
    @SerializedName("time")
    val time: String,
    
    @SerializedName("temperature_2m_max")
    val maxTemperature: String,
    
    @SerializedName("temperature_2m_min")
    val minTemperature: String,
    
    @SerializedName("precipitation_probability_max")
    val precipitationProbability: String
)

data class DailyWeather(
    @SerializedName("time")
    val time: List<String>,
    
    @SerializedName("temperature_2m_max")
    val maxTemperature: List<Double>,
    
    @SerializedName("temperature_2m_min")
    val minTemperature: List<Double>,
    
    @SerializedName("precipitation_probability_max")
    val precipitationProbability: List<Int>
)