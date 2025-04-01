package com.example.watherforecast.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/forecast.json")
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("aqi") aqi: String = "no",
        @Query("lang") lang: String = "pt"
    ): WeatherResponse
}

data class WeatherResponse(
    val forecast: Forecast
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avghumidity: Int,
    val maxwind_kph: Double,
    val daily_chance_of_rain: Int
) 