package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hu.bme.aut.android.vnnqwh.explorer.location.ProvideUserLocation
import hu.bme.aut.android.vnnqwh.explorer.navigation.Screen
import hu.bme.aut.android.vnnqwh.explorer.ui.common.ExplorerAppBar
import hu.bme.aut.android.vnnqwh.explorer.ui.common.ExplorerSearchBar
import hu.bme.aut.android.vnnqwh.explorer.ui.common.PlaceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttractionsListScreen(
    navController: NavHostController,
    onSignOut: () -> Unit,
    onMapsClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    viewModel: AttractionListViewModel = hiltViewModel()
) {
    val searchText = remember { mutableStateOf("") }
    val places by viewModel.places.collectAsState()
    val loading by viewModel.loading.collectAsState()

    ProvideUserLocation { latitude, longitude ->
        viewModel.fetchNearbyAttractions(latitude, longitude)
    }

    val filteredPlaces = if (searchText.value.isNotEmpty()) {
        places.filter { place ->
            place.name?.contains(searchText.value, ignoreCase = true) == true
        }
    } else {
        places
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                ExplorerAppBar(
                    title = "Attractions",
                    actions = {
                        IconButton(onClick = {
                            viewModel.signOut()
                            onSignOut()
                        }) {
                            Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                        }
                        IconButton(onClick = { onMapsClicked() }) {
                            Icon(imageVector = Icons.Default.Map, contentDescription = null)
                        }
                        IconButton(onClick = { onFavoritesClicked() }) {
                            Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = null)
                        }
                    }
                )
                ExplorerSearchBar(
                    searchText = searchText,
                    onSearch = { query -> searchText.value = query }
                )
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else if (filteredPlaces.isEmpty()) {
                Text(text = "No attractions found")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredPlaces) { place ->
                        PlaceCard(place, onClick = {
                            navController.navigate(Screen.AttractionDetails.passId(place.id))
                        })
                    }
                }
            }
        }
    }
}
