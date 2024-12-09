    package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_details

    import android.graphics.Bitmap
    import androidx.lifecycle.ViewModel
    import dagger.hilt.android.lifecycle.HiltViewModel
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import javax.inject.Inject
    import android.util.Log
    import androidx.lifecycle.viewModelScope
    import com.google.android.libraries.places.api.net.FetchPlaceRequest
    import com.google.android.libraries.places.api.net.PlacesClient
    import com.google.android.libraries.places.api.net.kotlin.fetchPhotoRequest
    import hu.bme.aut.android.vnnqwh.explorer.feature.attraction_favorites.FavoritesRepository
    import hu.bme.aut.android.vnnqwh.explorer.network.model.Place
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.tasks.await

    @HiltViewModel
    class AttractionDetailsViewModel @Inject constructor(
        private val favoritesRepository: FavoritesRepository,
        private val placesClient: PlacesClient
    ) : ViewModel() {

        private val _placeDetails = MutableStateFlow<Place?>(null)
        val placeDetails: StateFlow<Place?> = _placeDetails

        fun toggleFavorite(attractionId: String){
            Log.d("AttractionDetailsViewModel", "Toggling favorite for attraction: $attractionId")
            viewModelScope.launch {
                try {
                    val isFavorite = favoritesRepository.isFavorite(attractionId)

                    if (isFavorite) {
                        favoritesRepository.removeFavorite(attractionId)
                    } else {
                        favoritesRepository.addFavorite(attractionId)
                    }
                    _placeDetails.value = _placeDetails.value?.copy(isFavorite = !isFavorite)
                } catch (e: Exception) {
                    Log.e("AttractionDetailsViewModel", "Error toggling favorite: ${e.message}")
                }
            }
        }

        fun loadAttractionDetails(attractionId: String) {
            if (attractionId.isEmpty()) {
                Log.e("AttractionDetailsViewModel", "Error: Attraction ID is empty!")
                return
            }

            val placeFields = listOf(
                com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.NAME,
                com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                com.google.android.libraries.places.api.model.Place.Field.PHOTO_METADATAS,
                com.google.android.libraries.places.api.model.Place.Field.PHONE_NUMBER,
                com.google.android.libraries.places.api.model.Place.Field.RATING,
                com.google.android.libraries.places.api.model.Place.Field.USER_RATINGS_TOTAL,
                com.google.android.libraries.places.api.model.Place.Field.OPENING_HOURS,
                com.google.android.libraries.places.api.model.Place.Field.WEBSITE_URI,
            )

            val request = FetchPlaceRequest.builder(attractionId, placeFields).build()

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val place = response.place
                    val photoMetadatas = place.photoMetadatas

                    if (photoMetadatas.isNullOrEmpty()) {
                        updatePlaceDetails(place, emptyList())
                    } else {
                        val photoRequests = photoMetadatas.map { metadata ->
                            placesClient.fetchPhoto(fetchPhotoRequest(metadata))
                        }

                        viewModelScope.launch {
                            val photos = photoRequests.mapNotNull { request ->
                                try {
                                    request.await().bitmap
                                } catch (e: Exception) {
                                    Log.e("PhotoFetch", "Error fetching photo: ${e.message}")
                                    null
                                }
                            }

                            updatePlaceDetails(place, photos)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("AttractionDetailsViewModel", "Failed to fetch place details: ${exception.message}")
                }
        }

        private fun updatePlaceDetails(
            place: com.google.android.libraries.places.api.model.Place,
            photos: List<Bitmap>
        ) {
            val openingHours = place.openingHours?.weekdayText?.joinToString("\n")
            val placeDomain = Place(
                id = place.id ?: "",
                name = place.name ?: "",
                address = place.address ?: "",
                phoneNumber = place.phoneNumber,
                rating = place.rating,
                ratingCount = place.userRatingsTotal ?: 0,
                openingHours = openingHours,
                websiteUri = place.websiteUri?.toString(),
                photoUrls = photos,

            )
            _placeDetails.value = placeDomain
        }
    }

