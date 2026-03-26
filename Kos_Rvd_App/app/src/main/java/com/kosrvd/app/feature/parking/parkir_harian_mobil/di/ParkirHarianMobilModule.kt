package com.kosrvd.app.feature.parking.parkir_harian_mobil.di

import com.kosrvd.app.feature.parking.parkir_harian_mobil.data.repository.ParkirHarianMobilRepositoryImpl
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.repository.ParkirHarianMobilRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParkirHarianMobilModule {
    @Binds
    @Singleton
    abstract fun bindParkirHarianMobilRepository(parkirHarianMobilRepositoryImpl: ParkirHarianMobilRepositoryImpl): ParkirHarianMobilRepository
}