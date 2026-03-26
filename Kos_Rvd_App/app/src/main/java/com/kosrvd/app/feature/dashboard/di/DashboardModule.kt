package com.kosrvd.app.feature.dashboard.di

import com.kosrvd.app.feature.dashboard.domain.repository.RingkasanDashboardAdminRepository
import com.kosrvd.app.feature.dashboard.data.repository.RingkasanDashboardAdminRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {
    @Binds
    @Singleton
    abstract fun bindRingkasanDashboardAdminRepository(ringkasanDashboardAdminRepositoryImpl: RingkasanDashboardAdminRepositoryImpl): RingkasanDashboardAdminRepository

}