package com.example.watherforecast

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.watherforecast.ui.theme.WeatherForecastTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewModel.fetchWeatherData()
            }
            else -> {
                viewModel.setError("Permissão de localização é necessária para mostrar o clima da sua região.")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastTheme {
                val navController = rememberNavController()
                val weatherData by viewModel.weatherData.collectAsState()
                val isLoading by viewModel.isLoading.collectAsState()
                val error by viewModel.error.collectAsState()

                // Verificar permissões quando o app inicia
                LaunchedEffect(Unit) {
                    if (viewModel.hasLocationPermission()) {
                        viewModel.fetchWeatherData()
                    } else {
                        locationPermissionRequest.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }

                NavHost(navController = navController, startDestination = "weather_list") {
                    composable("weather_list") {
                        WeatherScreen(
                            weatherData = weatherData,
                            isLoading = isLoading,
                            error = error,
                            onRetry = { viewModel.fetchWeatherData() },
                            onWeatherDayClick = { weatherData ->
                                navController.navigate("weather_detail/${weatherData.date}")
                            }
                        )
                    }
                    composable("weather_detail/{date}") { backStackEntry ->
                        val date = backStackEntry.arguments?.getString("date")
                        weatherData?.result?.find { it.date == date }?.let { weatherData ->
                            WeatherDetailScreen(
                                weatherData = weatherData,
                                onBackPressed = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

