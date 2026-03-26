package com.kosrvd.app.feature.room.di

import com.kosrvd.app.feature.room.domain.repository.KamarRepository
import com.kosrvd.app.feature.room.data.repository.KamarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class KamarModule {
    @Binds
    @Singleton
    abstract fun bindKamarRepository(kamarRepositoryImpl: KamarRepositoryImpl): KamarRepository
}