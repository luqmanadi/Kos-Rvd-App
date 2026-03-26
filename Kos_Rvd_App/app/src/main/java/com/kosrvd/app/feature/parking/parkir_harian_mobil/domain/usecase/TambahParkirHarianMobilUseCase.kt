package com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.usecase

import android.util.Log
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.presentation.utils.convertMillisToTimeStamp
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.model.TambahParkirHarianMobil
import com.kosrvd.app.feature.parking.parkir_harian_mobil.domain.repository.ParkirHarianMobilRepository
import javax.inject.Inject

class TambahParkirHarianMobilUseCase @Inject constructor(
    private val parkirHarianMobilRepository: ParkirHarianMobilRepository
) {
    suspend operator fun invoke(
        idZonaParkir: String,
        userName: String,
        zoneName: String,
        numberPlate: String,
        carBrand: String,
        carName: String,
        notes: String?,
        startDate: Long,
        completionDate: Long,
        totalCost: Long
    ): Result<Unit, DataError> {
        val tambahParkirHarianMobilModel = TambahParkirHarianMobil(
            idZonaParkir = idZonaParkir,
            userName = userName,
            zoneName = zoneName,
            numberPlate = numberPlate,
            carBrand = carBrand,
            carName = carName,
            notes = notes,
            startDate = convertMillisToTimeStamp(startDate),
            completionDate = convertMillisToTimeStamp(completionDate),
            proofOfPayment = null,
            paymentStatus = Constant.BELUM_LUNAS,
            totalCost = totalCost,
            cancelledStatus = false
        )
        Log.d("Tambah Parkir", tambahParkirHarianMobilModel.toString())
        return parkirHarianMobilRepository.addParkirHarianMobil(tambahParkirHarianMobilModel)
    }
}