package com.kosrvd.app.feature.billing.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.usecase.CheckNetworkUseCase
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrNull
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import com.kosrvd.app.core.presentation.utils.calculateSmartDueDate
import com.kosrvd.app.feature.billing.domain.model.BuatTagihan
import com.kosrvd.app.feature.billing.domain.model.Diskon
import com.kosrvd.app.feature.billing.domain.model.ProrataDetail
import com.kosrvd.app.feature.billing.domain.model.Tagihan
import com.kosrvd.app.feature.billing.domain.repository.TagihanRepository
import javax.inject.Inject

class BuatTagihanUseCase @Inject constructor(
    private val tagihanRepository: TagihanRepository,
    private val checkNetworkUseCase: CheckNetworkUseCase
) {
    suspend operator fun invoke(
        item: Penyewaan,
        maintenanceFee: Boolean,
        periodStart: Timestamp,
        periodEnd: Timestamp,
        diskon: Diskon? = null,
        totalBill: Long,
        sumDayPeriodeBill: Int,
        prorataDetail: ProrataDetail? = null
    ): Result<Tagihan, DataError> {
        if (!checkNetworkUseCase()){
            return Result.Error(DataError.NETWORK_NO_INTERNET)
        }
        val isAlreadyExist = tagihanRepository.checkTagihanByIdPenyewa(item.idPenyewa, periodEnd)
            .onError { return Result.Error(it) }
            .getOrNull() ?: false

        if (isAlreadyExist){
            return Result.Error(DataError.TAGIHAN_SUDAH_TERDAFTAR)
        }

        val residentAccountIdList = item.listResident.map { it.idAkun }
        val residentNameList = item.listResident.map { it.name }
        val dueDate = calculateSmartDueDate(periodStart)
        val roomRentalFee = if (item.listResident.size > 1) {
            item.infoKamar.currentRoomRentalCost.twoPersons ?: item.infoKamar.currentRoomRentalCost.onePerson
        } else {
            item.infoKamar.currentRoomRentalCost.onePerson
        }
        val highPowerElectronicEquipmentUsageCosts = item.pemakaianAlatElektronikBulanan

        val modelBuatTagihan = BuatTagihan(
            idPenyewa = item.idPenyewa,
            numberRoom = item.infoKamar.numberRoom,
            residentAccountIdList = residentAccountIdList,
            residentNameList = residentNameList,
            periodStart = periodStart,
            periodEnd = periodEnd,
            diskon = diskon,
            dueDate = dueDate,
            roomRentalFee = roomRentalFee,
            carParkingRentalFeeMonthly = item.pemakaianParkirMobilBulanan?.zonaParkir?.monthlyFee,
            highPowerElectronicEquipmentUsageCostsMonthly = highPowerElectronicEquipmentUsageCosts,
            maintenanceFee = maintenanceFee,
            billAmount = totalBill,
            paymentStatus = Constant.BELUM_LUNAS,
            proofOfPayment = null,
            rejectionStatement = null,
            dateUploadProof = null,
            verificationDate = null,
            datePaidOff = null,
            dateCreated = Timestamp.now(),
            sumDayPeriodeBill = sumDayPeriodeBill,
            prorataDetail = prorataDetail
        )

        return tagihanRepository.buatTagihan(modelBuatTagihan)
    }
}