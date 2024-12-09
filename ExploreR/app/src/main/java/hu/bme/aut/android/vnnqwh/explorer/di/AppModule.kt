package hu.bme.aut.android.vnnqwh.explorer.di

import android.app.Application
import android.content.Context
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService
import hu.bme.aut.android.vnnqwh.explorer.feature.attraction_favorites.FavoritesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance("https://explorer-e6b86-default-rtdb.europe-west1.firebasedatabase.app")

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        firebaseDatabase: FirebaseDatabase,
        authService: AuthService
    ): FavoritesRepository {
        return FavoritesRepository(firebaseDatabase, authService)
    }

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }
}

