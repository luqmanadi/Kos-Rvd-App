package com.kosrvd.app.feature.management.di

import com.kosrvd.app.feature.management.data.repository.KamarRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.KeluhanRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.NotificationRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.ParkirHarianMobilRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.PengaturanRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.PengumumanRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.PenyewaanRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.RingkasanDashboardAdminRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.TagihanRepositoryImpl
import com.kosrvd.app.feature.management.data.repository.ZonaParkiranMobilRepositoryImpl
import com.kosrvd.app.feature.management.domain.repository.KamarRepository
import com.kosrvd.app.feature.management.domain.repository.KeluhanRepository
import com.kosrvd.app.feature.management.domain.repository.NotificationRepository
import com.kosrvd.app.feature.management.domain.repository.ParkirHarianMobilRepository
import com.kosrvd.app.feature.management.domain.repository.PengaturanRepository
import com.kosrvd.app.feature.management.domain.repository.PengumumanRepository
import com.kosrvd.app.feature.management.domain.repository.PenyewaanRepository
import com.kosrvd.app.feature.management.domain.repository.RingkasanDashboardAdminRepository
import com.kosrvd.app.feature.management.domain.repository.TagihanRepository
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import com.kosrvd.app.feature.management.domain.usecase.CalculateTotalBillUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagementModule {

    @Binds
    @Singleton
    abstract fun bindTagihanRepository(tagihanRepositoryImpl: TagihanRepositoryImpl): TagihanRepository

    @Binds
    @Singleton
    abstract fun bindKeluhanRepository(keluhanRepositoryImpl: KeluhanRepositoryImpl): KeluhanRepository

    @Binds
    @Singleton
    abstract fun bindPengumumanRepository(pengumumanRepositoryImpl: PengumumanRepositoryImpl): PengumumanRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindRingkasanDashboardAdminRepository(ringkasanDashboardAdminRepositoryImpl: RingkasanDashboardAdminRepositoryImpl): RingkasanDashboardAdminRepository

    @Binds
    @Singleton
    abstract fun bindPenyewaanRepository(penyewaanRepositoryImpl: PenyewaanRepositoryImpl): PenyewaanRepository

    @Binds
    @Singleton
    abstract fun bindKamarRepository(kamarRepositoryImpl: KamarRepositoryImpl): KamarRepository

    @Binds
    @Singleton
    abstract fun bindZonaParkiranMobilRepository(zonaParkiranMobilRepositoryImpl: ZonaParkiranMobilRepositoryImpl): ZonaParkiranMobilRepository

    @Binds
    @Singleton
    abstract fun bindParkirHarianMobilRepository(parkirHarianMobilRepositoryImpl: ParkirHarianMobilRepositoryImpl): ParkirHarianMobilRepository

    @Binds
    @Singleton
    abstract fun bindPengaturanRepository(pengaturanRepositoryImpl: PengaturanRepositoryImpl): PengaturanRepository
}