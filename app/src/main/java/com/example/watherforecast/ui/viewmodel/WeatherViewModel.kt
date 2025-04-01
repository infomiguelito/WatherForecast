package com.example.watherforecast.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.watherforecast.data.repository.WeatherRepository
import com.example.watherforecast.domain.model.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weatherState = MutableStateFlow<List<Weather>>(emptyList())
    val weatherState: StateFlow<List<Weather>> = _weatherState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadWeatherData()
    }

    fun loadWeatherData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.getWeatherForecast().collect { weatherList ->
                    _weatherState.value = weatherList
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun getWeatherByDate(date: String): Weather? {
        return _weatherState.value.find { it.date == date }
    }

    fun refreshWeatherData() {
        loadWeatherData()
    }

    fun clearStaleData(timestamp: Long) {
        viewModelScope.launch {
            repository.clearStaleData(timestamp)
        }
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