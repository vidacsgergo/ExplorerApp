package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_map

import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.Places
import javax.inject.Inject
import android.Manifest
import android.content.pm.PackageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.vnnqwh.explorer.network.model.Place as DomainPlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import androidx.core.app.ActivityCompat
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import hu.bme.aut.android.vnnqwh.explorer.network.model.toDomainModel


@HiltViewModel
class AttractionsMapViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    private val placesClient = Places.createClient(context)

    private val _places = MutableStateFlow<List<DomainPlace>>(emptyList())
    val places: StateFlow<List<DomainPlace>> = _places

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchNearbyAttractions(latitude: Double, longitude: Double, radius: Int = 1000) {
        _loading.value = true
        _errorMessage.value = null

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )

            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            placesClient.findCurrentPlace(request)
                .addOnSuccessListener { response ->
                    _places.value = response.placeLikelihoods.map { it.place.toDomainModel() }
                    _loading.value = false
                }
                .addOnFailureListener { exception ->
                    _places.value = emptyList()
                    _errorMessage.value = "Error while loading attractions: ${exception.message}"
                    _loading.value = false
                }
        } else {
            _errorMessage.value = "No location permission"
            _loading.value = false
        }
    }
}
