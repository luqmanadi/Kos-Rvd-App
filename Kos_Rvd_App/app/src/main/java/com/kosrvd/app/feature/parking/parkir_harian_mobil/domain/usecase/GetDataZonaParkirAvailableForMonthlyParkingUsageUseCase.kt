package com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.usecase

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.model.ZonaParkiran
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.repository.ParkirHarianMobilRepository
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.repository.ZonaParkiranMobilRepository
import javax.inject.Inject

class GetDataZonaParkirAvailableForMonthlyParkingUsageUseCase @Inject constructor(
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository,
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository
){
    suspend operator fun invoke(): Result<List<ZonaParkiran>, DataError>{
        val resultZonaParkiranKosong = zonaParkiranMobilRepository.getAllZonaParkiranMobilStatusKosong().getOrElse { return Result.Error(it) }
        val busyDailyZones = parkirHarianMobilRepository.getAllParkirHarianMobilCancelledFalseAndCompletionDateGreaterThan().getOrElse { return Result.Error(it) }
        val busyZoneIds = busyDailyZones.map { it.idZonaParkir }.toSet()
        val availableZonesForMonthly = resultZonaParkiranKosong.filter { it.idZonaParkir !in busyZoneIds }

        return Result.Success(availableZonesForMonthly)
    }
}