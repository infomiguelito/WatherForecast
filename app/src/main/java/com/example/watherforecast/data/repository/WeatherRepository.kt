package com.example.watherforecast.data.repository

import android.content.Context
import android.util.Log
import com.example.watherforecast.data.remote.ApiService
import com.example.watherforecast.domain.model.Weather
import com.example.watherforecast.util.LocationHelper
import com.example.watherforecast.util.WeatherMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepository(
    private val context: Context,
    private val locationHelper: LocationHelper,
    private val apiService: ApiService
) {
    fun hasLocationPermission(): Boolean = locationHelper.hasLocationPermission()

    fun isLocationEnabled(): Boolean = locationHelper.isLocationEnabled()

    fun getWeatherData(): Flow<List<Weather>> = flow {
        try {
            Log.d("WeatherRepository", "Fetching weather data")
            val location = locationHelper.getCurrentLocation()
            
            if (location == null) {
                Log.e("WeatherRepository", "Location is null")
                throw Exception("Não foi possível obter sua localização")
            }

            Log.d("WeatherRepository", "Location obtained: ${location.latitude}, ${location.longitude}")
            val response = apiService.getWeatherForecast(location.latitude, location.longitude)

            if (response.isSuccessful && response.body() != null) {
                Log.d("WeatherRepository", "Weather data fetched successfully")
                val weatherList = WeatherMapper.mapResponseToWeatherList(response.body()!!)
                emit(weatherList)
            } else {
                Log.e("WeatherRepository", "Error fetching weather data: ${response.code()}")
                throw Exception("Erro ao buscar dados do clima: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error in getWeatherData", e)
            throw e
        }
    }
} 