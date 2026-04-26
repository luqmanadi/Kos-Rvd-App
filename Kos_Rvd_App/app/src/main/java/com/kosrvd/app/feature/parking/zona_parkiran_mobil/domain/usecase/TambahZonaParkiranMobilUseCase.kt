package com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.usecase

import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrElse
import com.kosrvd.app.feature.parking.zona_parkiran_mobil.domain.repository.ZonaParkiranMobilRepository
import javax.inject.Inject

class TambahZonaParkiranMobilUseCase @Inject constructor(
    private val checkNetworkUseCase: CheckNetworkUseCase,
    private val zonaParkiranMobilRepository: ZonaParkiranMobilRepository
) {
    suspend operator fun invoke(
        zoneName: String,
        monthlyFee: Long,
        dailyCosts: Long
    ): Result<Unit, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }
        val isNamaZonaExist = zonaParkiranMobilRepository.getZonaParkirByZoneName(zoneName)
            .getOrElse { return Result.Error(it) }

        return if (isNamaZonaExist.isEmpty()) {
            zonaParkiranMobilRepository.addZonaParkiranMobil(
                zoneName = zoneName,
                monthlyFee = monthlyFee,
                dailyCosts = dailyCosts
            )
        } else {
            Result.Error(DataError.ZONA_PARKIRAN_TERPAKAI)
        }
    }
}