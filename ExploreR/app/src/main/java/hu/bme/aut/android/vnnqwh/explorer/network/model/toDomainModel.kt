package hu.bme.aut.android.vnnqwh.explorer.network.model

fun com.google.android.libraries.places.api.model.Place.toDomainModel(): Place {
    return Place(
        id = this.id ?: "No ID",
        name = this.name ?: "Unknown",
        address = this.address ?: "Unknown Address",
        geometry = Geometry(
            location = Location(
                lat = this.latLng?.latitude ?: 0.0,
                lng = this.latLng?.longitude ?: 0.0
            )
        ),
    )
}
