package com.kosrvd.app.feature.billing.di

import com.kosrvd.app.feature.billing.domain.repository.TagihanRepository
import com.kosrvd.app.feature.billing.data.repository.TagihanRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TagihanModule {
    @Binds
    @Singleton
    abstract fun bindTagihanRepository(tagihanRepositoryImpl: TagihanRepositoryImpl): TagihanRepository
}