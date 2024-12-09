package hu.bme.aut.android.vnnqwh.explorer.data.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.vnnqwh.explorer.data.auth.AuthService
import hu.bme.aut.android.vnnqwh.explorer.data.auth.FirebaseAuthService
import hu.bme.aut.android.vnnqwh.explorer.domain.usecases.PasswordsMatchUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthService(firebaseAuth: FirebaseAuth): AuthService = FirebaseAuthService(firebaseAuth)

    @Provides
    @Singleton
    fun providePasswordsMatchUseCase(): PasswordsMatchUseCase {
        return PasswordsMatchUseCase()
    }
}