package com.example.watherforecast

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.watherforecast.ui.theme.WeatherForecastTheme

class MainActivity : ComponentActivity() {
    private lateinit var locationHelper: LocationHelper
    private lateinit var apiService: ApiService

    // Estados
    private val weatherData = mutableStateOf<WeatherResponse?>(null)
    private val isLoading = mutableStateOf(true)
    private val error = mutableStateOf<String?>(null)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Permissão concedida, buscar dados
                lifecycleScope.launch {
                    fetchWeatherData()
                }
            }
            else -> {
                error.value = "Precisamos da permissão de localização para mostrar o clima local"
                isLoading.value = false
            }
        }
    }

    private suspend fun fetchWeatherData() {
        if (!locationHelper.isLocationEnabled()) {
            error.value = "Por favor, ative a localização do dispositivo"
            isLoading.value = false
            return
        }

        isLoading.value = true
        error.value = null

        try {
            val location = locationHelper.getCurrentLocation()
            if (location != null) {
                val response = apiService.getWeatherForecast(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                weatherData.value = response.toWeatherResponse()
            } else {
                error.value = "Não foi possível obter sua localização"
            }
        } catch (e: Exception) {
            error.value = "Erro ao buscar dados do clima: ${e.message}"
        }
        isLoading.value = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        locationHelper = LocationHelper(this)
        apiService = RetrofitClient.createService(ApiService::class.java)

        setContent {
            LaunchedEffect(Unit) {
                if (locationHelper.hasLocationPermission()) {
                    fetchWeatherData()
                } else {
                    locationPermissionRequest.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }

            WeatherForecastTheme {
                WeatherScreen(
                    weatherData = weatherData.value,
                    isLoading = isLoading.value,
                    error = error.value
                )
            }
        }
    }
}

