package com.example.watherforecast.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather ORDER BY date ASC")
    fun getAllWeather(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM weather WHERE date = :date")
    suspend fun getWeatherByDate(date: String): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWeather(weatherList: List<WeatherEntity>)

    @Query("DELETE FROM weather")
    suspend fun deleteAllWeather()

    @Query("SELECT * FROM weather WHERE lastUpdated < :timestamp")
    suspend fun getStaleWeather(timestamp: Long): List<WeatherEntity>
} 