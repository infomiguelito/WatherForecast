package com.example.watherforecast

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val locationHelper = LocationHelper(application)
    private val apiService = RetrofitClient.createService(ApiService::class.java)

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun hasLocationPermission(): Boolean {
        return locationHelper.hasLocationPermission()
    }

    fun fetchWeatherData() {
        viewModelScope.launch {
            if (!locationHelper.isLocationEnabled()) {
                setError("Por favor, ative a localização do dispositivo")
                return@launch
            }

            _isLoading.value = true
            _error.value = null

            try {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    Log.d("WeatherViewModel", "Buscando dados para latitude: ${location.latitude}, longitude: ${location.longitude}")
                    val response = apiService.getWeatherForecast(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    Log.d("WeatherViewModel", "Resposta recebida: $response")
                    val weatherResponse = response.toWeatherResponse()
                    Log.d("WeatherViewModel", "Dados convertidos: $weatherResponse")
                    _weatherData.value = weatherResponse
                } else {
                    setError("Não foi possível obter sua localização")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Erro ao buscar dados", e)
                setError("Erro ao buscar dados do clima: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun setError(message: String) {
        _error.value = message
        _isLoading.value = false
    }
} 