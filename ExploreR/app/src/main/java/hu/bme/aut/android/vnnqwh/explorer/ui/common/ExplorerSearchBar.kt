package hu.bme.aut.android.vnnqwh.explorer.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExplorerSearchBar(
    searchText: MutableState<String>,
    onSearch: (String) -> Unit
) {
    TextField(
        value = searchText.value,
        onValueChange = onSearch,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text(text = "Search") }
    )
}