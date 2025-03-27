package com.example.watherforecast.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.watherforecast.domain.model.Weather
import com.example.watherforecast.ui.screens.detail.WeatherDetailScreen
import com.example.watherforecast.ui.screens.list.WeatherListScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    weatherData: List<Weather>?,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit
) {
    NavHost(navController = navController, startDestination = "weather_list") {
        composable("weather_list") {
            WeatherListScreen(
                weatherData = weatherData,
                isLoading = isLoading,
                error = error,
                onRetry = onRetry,
                onWeatherDayClick = { weather ->
                    navController.navigate("weather_detail/${weather.date}")
                }
            )
        }
        composable("weather_detail/{date}") { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date")
            weatherData?.find { it.date == date }?.let { weather ->
                WeatherDetailScreen(
                    weatherData = weather,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
} 