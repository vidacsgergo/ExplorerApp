package hu.bme.aut.android.vnnqwh.explorer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService
import hu.bme.aut.android.vnnqwh.explorer.navigation.NavGraph
import hu.bme.aut.android.vnnqwh.explorer.ui.theme.ExploreRTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authService: AuthService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExploreRTheme {
                NavGraph(authService = authService)
            }
        }

        val apiKey = getString(R.string.google_maps_key)
        if (!Places.isInitialized())
            Places.initializeWithNewPlacesApiEnabled(applicationContext, apiKey)
    }
}

