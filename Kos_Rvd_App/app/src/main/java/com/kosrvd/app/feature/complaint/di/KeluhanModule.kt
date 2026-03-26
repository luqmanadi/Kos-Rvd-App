package com.kosrvd.app.feature.complaint.di

import com.kosrvd.app.feature.complaint.data.repository.KeluhanRepositoryImpl
import com.kosrvd.app.feature.complaint.domain.repository.KeluhanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KeluhanModule {
    @Binds
    @Singleton
    abstract fun bindKeluhanRepository(keluhanRepositoryImpl: KeluhanRepositoryImpl): KeluhanRepository
}