package com.kosrvd.app.feature.management.domain.usecase

import android.util.Log
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran
import com.kosrvd.app.feature.management.domain.repository.ParkirHarianMobilRepository
import com.kosrvd.app.feature.management.domain.repository.ZonaParkiranMobilRepository
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear
import javax.inject.Inject

class GetListZonaParkirHarianUseCase @Inject constructor(
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository,
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository
){
    suspend operator fun invoke(
        startDate: Long,
        completionDate: Long
    ): Result<List<ZonaParkiran>, DataError> {
        val allZones = zonaParkiranMobilRepository.getAllZonaParkiranMobilStatusKosong()
            .getOrElse { return Result.Error(it) }

        val activePemakaianParkirHarianMobil = parkirHarianMobilRepository.getAllParkirHarianMobilCancelledStatusFalse()
            .getOrElse { return Result.Error(it) }

        val occupiedZoneIds = activePemakaianParkirHarianMobil.filter { data ->
            val startDateParkirHarianMobil = data.startDate.toDate().time
            val completionDateParkirHarianMobil = data.completionDate.toDate().time

            startDate <= completionDateParkirHarianMobil && completionDate >= startDateParkirHarianMobil
        }.map { it.idZonaParkir }.toSet()
        Log.d("List Zone UC", "Start Date: ${startDate.toDayMonthShortAndYear()}, Completion Date: ${completionDate.toDayMonthShortAndYear()}")
        Log.d("List Zone UC", "Occupied Zone IDs: $occupiedZoneIds")

        val availableZones = allZones.filter { it.idZonaParkir !in occupiedZoneIds }

        return Result.Success(availableZones)
    }
}