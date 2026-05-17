package com.example.weatherappdanial.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.example.weatherappdanial.domain.location_model.Location
import com.example.weatherappdanial.domain.repository.LocationRepository
import com.example.weatherappdanial.domain.result.LocationError
import com.example.weatherappdanial.domain.result.Result
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepositoryImpl(
    private val context: Context,
    private val fusedClient: FusedLocationProviderClient
) : LocationRepository {

    override suspend fun getCurrentLocation(): Result<Location, LocationError> {

        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            return Result.Error(LocationError.NO_PERMISSION)
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            return Result.Error(LocationError.GPS_DISABLED)
        }

        return suspendCancellableCoroutine { continuation ->
            val cancellationTokenSource = CancellationTokenSource()

            continuation.invokeOnCancellation {
                cancellationTokenSource.cancel()
            }

            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).apply {
                addOnSuccessListener { androidLocation ->
                    if (androidLocation != null) {
                        continuation.resume(
                            Result.Success(
                                Location(
                                    lat = androidLocation.latitude,
                                    lon = androidLocation.longitude
                                )
                            )
                        )
                    } else {
                        fetchLastLocation(continuation)
                    }
                }
                addOnFailureListener {
                    continuation.resume(Result.Error(LocationError.UNKNOWN))
                }
            }
        }
    }

    private fun fetchLastLocation(
        continuation: kotlin.coroutines.Continuation<Result<Location, LocationError>>
    ) {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFine) {
            continuation.resume(Result.Error(LocationError.NO_PERMISSION))
            return
        }

        fusedClient.lastLocation
            .addOnSuccessListener { androidLocation ->
                if (androidLocation != null) {
                    continuation.resume(
                        Result.Success(
                            Location(
                                lat = androidLocation.latitude,
                                lon = androidLocation.longitude
                            )
                        )
                    )
                } else {
                    continuation.resume(Result.Error(LocationError.UNKNOWN))
                }
            }
            .addOnFailureListener {
                continuation.resume(Result.Error(LocationError.UNKNOWN))
            }
    }
}