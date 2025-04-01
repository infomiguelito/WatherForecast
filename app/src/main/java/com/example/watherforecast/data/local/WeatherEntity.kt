package com.example.watherforecast.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.watherforecast.domain.model.Weather

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val precipitationProbability: Int,
    val humidity: Int,
    val windSpeed: Double,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun toWeather(): Weather {
        return Weather(
            date = date,
            maxTemp = maxTemp,
            minTemp = minTemp,
            precipitationProbability = precipitationProbability,
            humidity = humidity,
            windSpeed = windSpeed
        )
    }

    companion object {
        fun fromWeather(weather: Weather): WeatherEntity {
            return WeatherEntity(
                date = weather.date,
                maxTemp = weather.maxTemp,
                minTemp = weather.minTemp,
                precipitationProbability = weather.precipitationProbability,
                humidity = weather.humidity,
                windSpeed = weather.windSpeed
            )
        }
    }
} 