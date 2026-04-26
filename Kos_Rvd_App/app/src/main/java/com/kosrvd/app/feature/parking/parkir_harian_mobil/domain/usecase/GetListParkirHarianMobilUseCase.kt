package com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.map
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.model.ParkirHarianMobil
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.repository.ParkirHarianMobilRepository
import com.kosrvd.app.feature.parking.parkir_harian_mobil.presentation.models.ListParkirHarianMobilUi
import javax.inject.Inject

class GetListParkirHarianMobilUseCase @Inject constructor(
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository,
    private val checkNetworkUseCase: CheckNetworkUseCase
) {
    suspend operator fun invoke(): Result<List<ListParkirHarianMobilUi>, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }
        val result = parkirHarianMobilRepository.getAllParkirHarianMobil()

        return result.map { listParkir ->
            val now = Timestamp.now()

            val mappedList = listParkir
                .filter { data ->
                    data.userName.isNotBlank() && data.zoneName.isNotBlank() && data.numberPlate.isNotBlank()
                }
                .map { data ->
                    val calculatedStatus = determineStatus(data, now)

                    ListParkirHarianMobilUi(
                        idParkirHarianMobil = data.idParkirHarianMobil,
                        userName = data.userName,
                        zoneName = data.zoneName,
                        numberPlate = data.numberPlate,
                        status = calculatedStatus
                    )
            }

            mappedList.sortedBy { getStatusSortWeight(it.status) }
        }
    }

    private fun determineStatus(data: ParkirHarianMobil, now: Timestamp): String {
        return when {
            data.cancelledStatus -> Constant.DIBATALKAN
            now < data.startDate -> Constant.DIPESAN
            now <= data.completionDate -> Constant.DIPAKAI
            else -> Constant.SELESAI
        }
    }

    private fun getStatusSortWeight(status: String): Int {
        return when(status) {
            Constant.DIPAKAI -> 1
            Constant.DIPESAN -> 2
            Constant.SELESAI -> 3
            Constant.DIBATALKAN -> 4
            else -> 5
        }
    }
}