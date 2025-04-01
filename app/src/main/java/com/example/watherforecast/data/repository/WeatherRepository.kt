package com.example.watherforecast.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.watherforecast.data.remote.ApiService
import com.example.watherforecast.data.local.WeatherDao
import com.example.watherforecast.data.local.WeatherEntity
import com.example.watherforecast.domain.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
    private val context: Context
) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val TAG = "WeatherRepository"

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun getWeatherData(): Flow<List<WeatherEntity>> {
        return weatherDao.getAllWeather()
    }

    suspend fun getWeatherForecast(): Flow<List<Weather>> = flow {
        try {
            // Primeiro, tenta obter dados do cache local
            val cachedWeather = weatherDao.getAllWeather().first()
            if (cachedWeather.isNotEmpty()) {
                emit(cachedWeather.map { it.toWeather() })
            }

            // Verifica permissões de localização
            if (!hasLocationPermission()) {
                Log.e(TAG, "Permissão de localização não concedida")
                return@flow
            }

            // Obtém a localização atual
            val location = withContext(Dispatchers.IO) {
                try {
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } catch (e: SecurityException) {
                    Log.e(TAG, "Erro ao obter localização: ${e.message}")
                    null
                }
            }

            if (location == null) {
                Log.e(TAG, "Não foi possível obter a localização")
                return@flow
            }

            Log.d(TAG, "Localização obtida: ${location.latitude}, ${location.longitude}")

            // Faz a chamada à API
            val response = apiService.getWeatherForecast(
                location = "${location.latitude},${location.longitude}",
                days = 7,
                aqi = "no",
                lang = "pt"
            )

            // Mapeia a resposta para o modelo de domínio
            val weatherList = response.forecast.forecastday.map { forecastDay ->
                Weather(
                    date = forecastDay.date,
                    maxTemp = forecastDay.day.maxtemp_c,
                    minTemp = forecastDay.day.mintemp_c,
                    humidity = forecastDay.day.avghumidity,
                    windSpeed = forecastDay.day.maxwind_kph,
                    precipitationProbability = forecastDay.day.daily_chance_of_rain
                )
            }

            // Salva no banco de dados local
            weatherList.forEach { weather ->
                weatherDao.insertWeather(WeatherEntity.fromWeather(weather))
            }

            // Emite a lista atualizada
            emit(weatherList)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao buscar previsão do tempo: ${e.message}")
            throw e
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getWeatherByDate(date: String): Weather? {
        return weatherDao.getWeatherByDate(date)?.toWeather()
    }

    suspend fun clearStaleData(timestamp: Long) {
        val staleData = weatherDao.getStaleWeather(timestamp)
        if (staleData.isNotEmpty()) {
            Log.d(TAG, "Removendo ${staleData.size} registros desatualizados")
            weatherDao.deleteAllWeather()
        }
    }
} 