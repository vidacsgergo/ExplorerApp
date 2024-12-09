package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_list

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService

import hu.bme.aut.android.vnnqwh.explorer.network.model.Place
import hu.bme.aut.android.vnnqwh.explorer.network.model.toDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttractionListViewModel @Inject constructor(
    private val context: Context,
    private val placesClient: PlacesClient,
    private val authService: AuthService
) : ViewModel() {

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun fetchNearbyAttractions(latitude: Double, longitude: Double, radius: Int = 1000) {
        _loading.value = true

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val request = FindCurrentPlaceRequest.builder(
                listOf(
                    com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME,
                    com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                    com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                    com.google.android.libraries.places.api.model.Place.Field.TYPES
                )
            ).build()
            ///*
            placesClient.findCurrentPlace(request)
                .addOnSuccessListener { response ->
                    _places.value = response.placeLikelihoods.map { it.place.toDomainModel() }
                    _loading.value = false
                }
                .addOnFailureListener {
                    _places.value = emptyList()
                    _loading.value = false
                }
            //*/
           /* placesClient.findCurrentPlace(request)
                .addOnSuccessListener { response ->
                    val filteredPlaces = response.placeLikelihoods.filter { likelihood ->
                        likelihood.place.types?.contains(com.google.android.libraries.places.api.model.Place.Type.TOURIST_ATTRACTION) == true
                    }

                    _places.value = filteredPlaces.map { it.place.toDomainModel() }
                    _loading.value = false
                }
                .addOnFailureListener { exception ->
                    Log.e("AttractionListViewModel", "Error fetching places: ${exception.message}")
                    _places.value = emptyList()
                    _loading.value = false
                }*/
        } else {
            Log.e("AttractionListViewModel", "Permission not granted for location access")
            _loading.value = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authService.signOut()
        }
    }
}