package com.example.watherforecast.ui.screens.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.watherforecast.data.repository.WeatherRepository
import com.example.watherforecast.domain.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherData = MutableStateFlow<List<Weather>?>(null)
    val weatherData: StateFlow<List<Weather>?> = _weatherData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchWeatherData()
    }

    fun hasLocationPermission(): Boolean = repository.hasLocationPermission()

    fun fetchWeatherData() {
        if (!repository.isLocationEnabled()) {
            setError("Por favor, ative a localização do dispositivo")
            return
        }

        viewModelScope.launch {
            repository.getWeatherData()
                .onStart { 
                    _isLoading.value = true
                    _error.value = null
                }
                .catch { e ->
                    setError(e.message ?: "Erro desconhecido")
                }
                .collect { weatherList ->
                    _weatherData.value = weatherList
                    _isLoading.value = false
                }
        }
    }

    private fun setError(message: String) {
        _error.value = message
        _isLoading.value = false
    }

    class Factory(private val repository: WeatherRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                return WeatherViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 