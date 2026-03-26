package com.kosrvd.app.feature.rental.di

import com.kosrvd.app.feature.rental.data.repository.PenyewaanRepositoryImpl
import com.kosrvd.app.feature.rental.domain.repository.PenyewaanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PenyewaanModule {
    @Binds
    @Singleton
    abstract fun bindPenyewaanRepository(penyewaanRepositoryImpl: PenyewaanRepositoryImpl): PenyewaanRepository
}