package com.example.watherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.watherforecast.data.remote.RetrofitClient
import com.example.watherforecast.data.repository.WeatherRepository
import com.example.watherforecast.ui.navigation.NavGraph
import com.example.watherforecast.ui.screens.weather.WeatherViewModel
import com.example.watherforecast.ui.theme.WeatherForecastTheme
import com.example.watherforecast.util.LocationHelper

class MainActivity : ComponentActivity() {
    private lateinit var repository: WeatherRepository
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationHelper = LocationHelper(this)
        val apiService = RetrofitClient.createService()
        repository = WeatherRepository(this, locationHelper, apiService)
        
        setContent {
            WeatherForecastTheme {
                val navController = rememberNavController()
                val viewModel: WeatherViewModel = viewModel(
                    factory = WeatherViewModel.Factory(repository)
                )
                val weatherData by viewModel.weatherData.collectAsState()
                val isLoading by viewModel.isLoading.collectAsState()
                val error by viewModel.error.collectAsState()

                NavGraph(
                    navController = navController,
                    weatherData = weatherData,
                    isLoading = isLoading,
                    error = error,
                    onRetry = { viewModel.fetchWeatherData() }
                )
            }
        }
    }
}

