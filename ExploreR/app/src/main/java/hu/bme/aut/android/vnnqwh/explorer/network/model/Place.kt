package hu.bme.aut.android.vnnqwh.explorer.network.model

import android.graphics.Bitmap

data class Place(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val geometry: Geometry = Geometry(),
    val isFavorite: Boolean = false,
    val phoneNumber: String? = null,
    val rating: Double? = null,
    val ratingCount: Int = 0,
    val openingHours: String? = null,
    val websiteUri: String? = null,
    val photoUrls: List<Bitmap> = emptyList()
)

data class Geometry(
    val location: Location = Location()
)

data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

