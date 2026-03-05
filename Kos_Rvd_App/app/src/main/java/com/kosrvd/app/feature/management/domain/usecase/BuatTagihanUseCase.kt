package com.kosrvd.app.feature.management.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.getOrNull
import com.kosrvd.app.core.domain.utils.onError
import com.kosrvd.app.feature.management.domain.model.BuatTagihan
import com.kosrvd.app.feature.management.domain.model.Diskon
import com.kosrvd.app.feature.management.domain.model.Penyewaan
import com.kosrvd.app.feature.management.domain.model.ProrataDetail
import com.kosrvd.app.feature.management.domain.model.Tagihan
import com.kosrvd.app.feature.management.domain.repository.TagihanRepository
import com.kosrvd.app.feature.management.presentation.designsystem.utils.calculateSmartDueDate
import javax.inject.Inject

class BuatTagihanUseCase @Inject constructor(
    private val tagihanRepository: TagihanRepository
) {
    suspend operator fun invoke(
        item: Penyewaan,
        adminFees: Boolean,
        periodStart: Timestamp,
        periodEnd: Timestamp,
        diskon: Diskon? = null,
        totalBill: Long,
        sumDayPeriodeBill: Int,
        prorataDetail: ProrataDetail? = null
    ): Result<Tagihan, DataError> {
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
            adminFees = adminFees,
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