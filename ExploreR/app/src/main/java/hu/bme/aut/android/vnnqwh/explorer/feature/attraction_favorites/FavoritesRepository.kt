package hu.bme.aut.android.vnnqwh.explorer.feature.attraction_favorites

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val authService: AuthService
) {
    private val userId: String
        get() = authService.currentUserId ?: throw IllegalStateException("No user logged in")

    private val favoritesRef: DatabaseReference
        get() = firebaseDatabase.getReference("favorites").child(userId)

    suspend fun addFavorite(attractionId: String) {
        Log.d("FavoritesRepository", "Adding favorite for attraction: $attractionId")
        favoritesRef.child(attractionId).setValue(true).await()
        Log.d("FavoritesRepository", "Added favorite for attraction: $attractionId")
    }

    suspend fun removeFavorite(attractionId: String) {
        favoritesRef.child(attractionId).removeValue().await()
    }

    suspend fun isFavorite(attractionId: String): Boolean {
        val snapshot = favoritesRef.child(attractionId).get().await()
        return snapshot.exists()
    }

    fun getFavorites(): Flow<List<String>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favorites = snapshot.children.mapNotNull { it.key }
                trySend(favorites)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        favoritesRef.addValueEventListener(listener)
        awaitClose { favoritesRef.removeEventListener(listener) }
    }
}
