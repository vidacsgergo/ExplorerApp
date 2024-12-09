package hu.bme.aut.android.vnnqwh.explorer.ui.common

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.asImageBitmap

@Composable
    fun PhotoGallery(photos: List<Bitmap>) {
        LazyRow(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            items(photos) { photo ->
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(150.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Image(
                        bitmap = photo.asImageBitmap(),
                        contentDescription = "Photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }