    package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_details

    import android.annotation.SuppressLint
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material.icons.filled.Favorite
    import androidx.compose.material.icons.filled.FavoriteBorder
    import androidx.compose.material.icons.filled.Map
    import androidx.compose.material.icons.filled.Phone
    import androidx.compose.material.icons.filled.Search
    import androidx.compose.material.icons.filled.StarRate
    import androidx.compose.material3.CircularProgressIndicator
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp
    import androidx.hilt.navigation.compose.hiltViewModel
    import androidx.navigation.NavHostController
    import hu.bme.aut.android.vnnqwh.explorer.ui.common.ExplorerAppBar
    import hu.bme.aut.android.vnnqwh.explorer.ui.common.PhotoGallery

    @SuppressLint("DefaultLocale")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AttractionDetailsScreen(
        navController: NavHostController,
        attractionId: String,
        viewModel: AttractionDetailsViewModel = hiltViewModel()
    ) {
        val placeDetails by viewModel.placeDetails.collectAsState()

        LaunchedEffect(attractionId) {
            viewModel.loadAttractionDetails(attractionId)
        }

        Scaffold(
            topBar = {
                ExplorerAppBar(
                    title = placeDetails?.name ?: "Details",
                    actions = {
                        IconButton(onClick = { viewModel.toggleFavorite(attractionId) }) {
                            Icon(
                                imageVector = if (placeDetails?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.TopCenter
            ) {
                placeDetails?.let { place ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Default.Map, contentDescription = null)

                            Text(
                                text = " " + place.address,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.Bottom)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            place.phoneNumber?.let { phone ->
                                Icon(Icons.Default.Phone, contentDescription = null)
                                Text(
                                    text = " $phone",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.Bottom)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            place.websiteUri?.let { website ->
                                Icon(Icons.Default.Search, contentDescription = null)
                                Text(text = website,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.Bottom))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            place.rating?.let { rating ->
                                Icon(Icons.Default.StarRate, contentDescription = null)
                                Text(
                                    text = " ${String.format("%.1f", rating)} (${place.ratingCount} reviews)",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.Bottom)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (place.photoUrls.isNotEmpty()) {
                            PhotoGallery(photos = place.photoUrls)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        place.openingHours?.let { hours ->
                            Text(text = "Opening Hours:", style = MaterialTheme.typography.headlineSmall)
                            Text(text = "\n$hours", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                } ?: CircularProgressIndicator()
            }
        }
    }

