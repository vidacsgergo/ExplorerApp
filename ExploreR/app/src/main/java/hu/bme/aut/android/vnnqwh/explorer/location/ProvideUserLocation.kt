package hu.bme.aut.android.vnnqwh.explorer.location

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ProvideUserLocation(
    onLocationReceived: (latitude: Double, longitude: Double) -> Unit
) {
    val context = LocalContext.current
    val locationClient = LocationServices.getFusedLocationProviderClient(context)

    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    onLocationReceived(it.latitude, it.longitude)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        }
    }
}