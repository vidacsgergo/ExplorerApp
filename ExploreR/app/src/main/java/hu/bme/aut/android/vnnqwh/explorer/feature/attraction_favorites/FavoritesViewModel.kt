package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.vnnqwh.explorer.network.model.Place
import hu.bme.aut.android.vnnqwh.explorer.network.model.toDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Place>>(emptyList())
    val favorites: StateFlow<List<Place>> = _favorites

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            favoritesRepository.getFavorites().collect { favoriteIds ->
                val favoritePlaces = favoriteIds.mapNotNull { id ->
                    fetchPlaceDetails(id)
                }
                _favorites.value = favoritePlaces
            }
        }
    }

    private suspend fun fetchPlaceDetails(placeId: String): Place? {
        return try {
            val placeFields = listOf(
                com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.NAME,
                com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
            )
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()
            val response = placesClient.fetchPlace(request).await()
            response.place.toDomainModel().copy(isFavorite = true)
        } catch (e: Exception) {
            Log.e("FavoritesViewModel", "Error fetching place details: ${e.message}")
            null
        }
    }
}
