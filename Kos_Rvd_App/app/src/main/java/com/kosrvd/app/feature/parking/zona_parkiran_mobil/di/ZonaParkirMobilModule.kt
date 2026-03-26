package com.kosrvd.app.feature.parking.zona_parkiran_mobil.di

import com.kosrvd.app.feature.parking.zona_parkiran_mobil.data.repository.ZonaParkiranMobilRepositoryImpl
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.repository.ZonaParkiranMobilRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ZonaParkirMobilModule {
    @Binds
    @Singleton
    abstract fun bindZonaParkiranMobilRepository(zonaParkiranMobilRepositoryImpl: ZonaParkiranMobilRepositoryImpl): ZonaParkiranMobilRepository
}