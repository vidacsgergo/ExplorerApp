package hu.bme.aut.android.vnnqwh.explorer.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.vnnqwh.explorer.network.model.Place

@Composable
fun PlaceCard(place: Place, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{onClick()}
            .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row{
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = place.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = place.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            place.rating?.let { rating ->
                Icon(Icons.Default.StarRate, contentDescription = null)
                Text(
                    text = " ${String.format("%.1f", rating)} (${place.ratingCount} reviews)",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
        }
    }
}

