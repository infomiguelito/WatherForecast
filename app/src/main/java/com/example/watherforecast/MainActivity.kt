package com.example.watherforecast

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.watherforecast.data.local.WeatherDatabase
import com.example.watherforecast.data.remote.ApiService
import com.example.watherforecast.data.repository.WeatherRepository
import com.example.watherforecast.data.remote.RetrofitClient
import com.example.watherforecast.ui.screens.detail.WeatherDetailScreen
import com.example.watherforecast.ui.screens.list.WeatherListScreen
import com.example.watherforecast.ui.theme.WeatherForecastTheme
import com.example.watherforecast.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private lateinit var repository: WeatherRepository
    private lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        apiService = RetrofitClient.createService()
        val weatherDao = WeatherDatabase.getInstance(this).weatherDao
        repository = WeatherRepository(apiService, weatherDao, this)
        
        setContent {
            WeatherForecastTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: WeatherViewModel = viewModel(
                        factory = WeatherViewModel.Factory(repository)
                    )
                    
                    val weatherData by viewModel.weatherState.collectAsState()
                    val isLoading by viewModel.isLoading.collectAsState()
                    val error by viewModel.error.collectAsState()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "weather_list"
                    ) {
                        composable("weather_list") {
                            WeatherListScreen(
                                weatherData = weatherData,
                                isLoading = isLoading,
                                error = error,
                                onRetry = { viewModel.refreshWeatherData() },
                                onWeatherDayClick = { weather ->
                                    navController.navigate("weather_detail/${weather.date}")
                                }
                            )
                        }
                        
                        composable("weather_detail/{date}") { backStackEntry ->
                            val date = backStackEntry.arguments?.getString("date") ?: return@composable
                            val weather = weatherData.find { it.date == date }
                            WeatherDetailScreen(
                                weather = weather,
                                onBackPressed = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}

