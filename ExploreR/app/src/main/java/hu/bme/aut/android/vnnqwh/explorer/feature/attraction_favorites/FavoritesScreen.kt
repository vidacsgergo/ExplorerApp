package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hu.bme.aut.android.vnnqwh.explorer.navigation.Screen
import hu.bme.aut.android.vnnqwh.explorer.ui.common.ExplorerAppBar
import hu.bme.aut.android.vnnqwh.explorer.ui.common.PlaceCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    Scaffold(
        topBar = {
            ExplorerAppBar(
                title = "Favorites",
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (favorites.isEmpty()) {
                Text(text = "No favorites yet!")
            } else {
                LazyColumn {
                    items(favorites) { place ->
                        PlaceCard(place, onClick = {
                            navController.navigate(Screen.AttractionDetails.passId(place.id))
                        })
                    }
                }
            }
        }
    }
}



