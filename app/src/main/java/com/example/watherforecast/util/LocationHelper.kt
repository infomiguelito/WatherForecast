package com.example.watherforecast.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationHelper(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    suspend fun getCurrentLocation(): Location? = suspendCancellableCoroutine { continuation ->
        try {
            if (!hasLocationPermission()) {
                Log.d("LocationHelper", "Location permission not granted")
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            if (!isLocationEnabled()) {
                Log.d("LocationHelper", "Location services disabled")
                continuation.resume(null)
                return@suspendCancellableCoroutine
            }

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    Log.d("LocationHelper", "Location retrieved: $location")
                    continuation.resume(location)
                }
                .addOnFailureListener { exception ->
                    Log.e("LocationHelper", "Error getting location", exception)
                    continuation.resume(null)
                }
        } catch (e: SecurityException) {
            Log.e("LocationHelper", "Security exception while getting location", e)
            continuation.resume(null)
        }
    }
} 