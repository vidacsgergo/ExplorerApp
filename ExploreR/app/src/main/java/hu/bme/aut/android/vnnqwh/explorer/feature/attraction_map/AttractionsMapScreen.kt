package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import hu.bme.aut.android.vnnqwh.explorer.location.ProvideUserLocation
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.aut.android.vnnqwh.explorer.navigation.Screen
import hu.bme.aut.android.vnnqwh.explorer.ui.common.ExplorerAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionsMapScreen(
    navController: NavHostController,
    viewModel: AttractionsMapViewModel = hiltViewModel()
) {
    val userLocation = remember { mutableStateOf<Pair<Double, Double>?>(null) }
    val places = viewModel.places.collectAsState().value
    val loading = viewModel.loading.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value

    ProvideUserLocation { latitude, longitude ->
        userLocation.value = latitude to longitude
        viewModel.fetchNearbyAttractions(latitude, longitude)
    }
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    }
    errorMessage?.let {
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.fillMaxWidth()
        )
    }
    if (userLocation.value == null) {
        Text(
            text = "Locating...",
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
    }

    userLocation.value?.let { (latitude, longitude) ->
        val currentLocation = LatLng(latitude, longitude)

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(currentLocation, 14f)
        }

        Scaffold(
            topBar = {
                ExplorerAppBar(
                    title = "Maps",
                    actions = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = rememberMarkerState(position = currentLocation),
                        title = "You are here"
                    )
                    places.forEach { attraction ->
                        val position = LatLng(
                            attraction.geometry.location.lat,
                            attraction.geometry.location.lng
                        )
                        rememberMarkerState(position = position)
                        Marker(
                            contentDescription = attraction.name,
                            state = rememberMarkerState(position = position),
                            visible = true,
                            tag = attraction.name,
                            title = attraction.name,
                            snippet = attraction.address,
                            onClick = {
                                navController.navigate(Screen.AttractionDetails.passId(attraction.id))
                                true
                            },
                        )
                    }
                }
            }
        }
    }
}



