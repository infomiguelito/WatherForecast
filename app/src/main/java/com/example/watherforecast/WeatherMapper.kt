package com.example.watherforecast

fun WeatherForecastDto.toWeatherResponse(): WeatherResponse {
    val weatherDataList = mutableListOf<WeatherData>()
    
    // Adiciona dados atuais
    weatherDataList.add(
        WeatherData(
            temperature = current.temperature,
            humidity = current.humidity,
            windSpeed = current.windSpeed,
            date = current.time,
            maxTemperature = daily.maxTemperature.firstOrNull() ?: 0.0,
            minTemperature = daily.minTemperature.firstOrNull() ?: 0.0,
            precipitationProbability = daily.precipitationProbability.firstOrNull() ?: 0
        )
    )
    
    // Adiciona previsão dos próximos dias
    daily.time.indices.forEach { i ->
        if (i > 0) { // Pula o primeiro dia que já foi adicionado com os dados atuais
            weatherDataList.add(
                WeatherData(
                    temperature = 0.0, // Não temos temperatura atual para dias futuros
                    humidity = 0, // Não temos umidade para dias futuros
                    windSpeed = 0.0, // Não temos velocidade do vento para dias futuros
                    date = daily.time[i],
                    maxTemperature = daily.maxTemperature[i],
                    minTemperature = daily.minTemperature[i],
                    precipitationProbability = daily.precipitationProbability[i]
                )
            )
        }
    }
    
    return WeatherResponse(result = weatherDataList)
} 