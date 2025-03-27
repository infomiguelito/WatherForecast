package com.example.watherforecast.data.remote

import com.example.watherforecast.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,wind_speed_10m",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_probability_max",
        @Query("timezone") timezone: String = "auto"
    ): Response<WeatherResponse>
} 