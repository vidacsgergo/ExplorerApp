package hu.bme.aut.android.vnnqwh.explorer.domain.usecases.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.vnnqwh.explorer.domain.usecases.IsEmailValidUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideIsEmailValidUseCase(): IsEmailValidUseCase {
        return IsEmailValidUseCase()
    }
}