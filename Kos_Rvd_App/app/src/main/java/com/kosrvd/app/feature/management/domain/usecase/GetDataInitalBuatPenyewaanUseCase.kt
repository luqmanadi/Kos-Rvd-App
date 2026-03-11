package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.domain.repository.AccountRepository
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.management.domain.model.InitialBuatPenyewaan
import com.kosrvd.app.feature.management.domain.repository.KamarRepository
import com.kosrvd.app.feature.management.domain.repository.ParkirHarianMobilRepository
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import javax.inject.Inject

class GetDataInitalBuatPenyewaanUseCase @Inject constructor(
    private val kamarRepository: KamarRepository,
    private val accountRepository: AccountRepository,
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository,
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository
) {
    suspend operator fun invoke(): Result<InitialBuatPenyewaan, DataError> {
        // get data kamar status kosong
        val resultKamar = kamarRepository.getAllKamarStatusKosong().getOrElse { return Result.Error(it) }
        val resultPenghuni = accountRepository.getAllAccountRolePenghuniStatusAktifAndNomorKamarNull().getOrElse { return Result.Error(it) }
        val resultZonaParkiranKosong = zonaParkiranMobilRepository.getAllZonaParkiranMobilStatusKosong().getOrElse { return Result.Error(it) }
        val busyDailyZones = parkirHarianMobilRepository.getAllParkirHarianMobilCancelledFalseAndCompletionDateGreaterThan().getOrElse { return Result.Error(it) }
        val busyZoneIds = busyDailyZones.map { it.idZonaParkir }.toSet()
        val availableZonesForMonthly = resultZonaParkiranKosong.filter { it.idZonaParkir !in busyZoneIds }

        val initialBuatPenyewaan = InitialBuatPenyewaan(
            listKamar = resultKamar,
            listPenghuni = resultPenghuni,
            listZoneParking = availableZonesForMonthly
        )
        return Result.Success(initialBuatPenyewaan)
    }
}