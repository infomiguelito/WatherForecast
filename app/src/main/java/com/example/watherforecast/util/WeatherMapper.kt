package com.example.watherforecast.util

import com.example.watherforecast.data.model.WeatherResponse
import com.example.watherforecast.domain.model.Weather

object WeatherMapper {
    fun mapResponseToWeatherList(response: WeatherResponse): List<Weather> {
        val daily = response.daily
        return daily.dates.indices.map { index ->
            Weather(
                date = daily.dates[index],
                maxTemp = daily.maxTemps[index],
                minTemp = daily.minTemps[index],
                humidity = response.current.humidity,
                windSpeed = response.current.windSpeed,
                precipitationProbability = daily.precipitationProbabilities[index]
            )
        }
    }
} 