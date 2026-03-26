package com.kosrvd.app.feature.announcement.di

import com.kosrvd.app.feature.announcement.data.repository.PengumumanRepositoryImpl
import com.kosrvd.app.feature.announcement.domain.repository.PengumumanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PengumumanModule {
    @Binds
    @Singleton
    abstract fun bindPengumumanRepository(pengumumanRepositoryImpl: PengumumanRepositoryImpl): PengumumanRepository
}