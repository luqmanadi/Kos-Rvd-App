package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.feature.management.data.repository.dto.AlatElektronikDto
import com.kosrvd.app.feature.management.data.repository.dto.DiskonDto
import com.kosrvd.app.feature.management.data.repository.dto.ProrataDetailDto
import com.kosrvd.app.feature.management.data.repository.dto.TagihanDto
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.BuatTagihan
import com.kosrvd.app.feature.management.domain.model.Diskon
import com.kosrvd.app.feature.management.domain.model.ProrataDetail
import com.kosrvd.app.feature.management.domain.model.Tagihan
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toDayMonthShortAndYear

fun TagihanDto.toTagihan(): Tagihan {
    return Tagihan(
        adminFees = this.adminFees,
        billAmount = this.billAmount,
        periodEnd = this.periodEnd,
        periodStart = this.periodStart,
        diskon = this.diskon?.toDiskon(),
        carParkingRentalFeeMonthly = this.carParkingRentalFeeMonthly,
        dateCreated = this.dateCreated,
        datePaidOff = this.datePaidOff,
        dateUploadProof = this.dateUploadProof,
        dueDate = this.dueDate,
        highPowerElectronicEquipmentUsageCostsMonthly = this.highPowerElectronicEquipmentUsageCostsMonthly.map { it.toAlatElektronik() },
        idPenyewa = this.idPenyewa,
        idTagihan = this.idTagihan,
        numberRoom = this.numberRoom,
        paymentStatus = this.paymentStatus,
        proofOfPayment = this.proofOfPayment,
        rejectionStatement = this.rejectionStatement,
        residentAccountIdList = this.residentAccountIdList,
        residentNameList = this.residentNameList,
        roomRentalFee = this.roomRentalFee,
        verificationDate = this.verificationDate,
        sumDayPeriodeBill = this.sumDayPeriodeBill,
        prorataDetail = this.prorataDetail?.toProRataDetail(),
    )
}

fun ProrataDetailDto.toProRataDetail(): ProrataDetail {
    return ProrataDetail(
        proSewaKamar = this.proSewaKamar,
        proSewaParkir = this.proSewaParkir,
        proSewaElektronik = this.proSewaElektronik
    )
}

fun DiskonDto.toDiskon(): Diskon {
    return Diskon(
        percent = this.percent,
        price = this.price,
        description = this.description
    )
}

fun AlatElektronikDto.toAlatElektronik(): AlatElektronik{
    return AlatElektronik(
        cost = this.cost,
        toolName = this.toolName,
        origin = this.origin
    )
}


fun BuatTagihan.toTagihan(idTagihan: String): Tagihan{
    return Tagihan(
        idTagihan = idTagihan,
        idPenyewa = this.idPenyewa,
        numberRoom = this.numberRoom,
        residentAccountIdList = this.residentAccountIdList,
        residentNameList = this.residentNameList,
        periodStart = this.periodStart,
        periodEnd = this.periodEnd,
        diskon = this.diskon,
        dueDate = this.dueDate,
        roomRentalFee = this.roomRentalFee,
        carParkingRentalFeeMonthly = this.carParkingRentalFeeMonthly,
        highPowerElectronicEquipmentUsageCostsMonthly = this.highPowerElectronicEquipmentUsageCostsMonthly,
        adminFees = this.adminFees,
        billAmount = this.billAmount,
        paymentStatus = this.paymentStatus,
        proofOfPayment = this.proofOfPayment,
        rejectionStatement = this.rejectionStatement,
        dateUploadProof = this.dateUploadProof,
        verificationDate = this.verificationDate,
        datePaidOff = this.datePaidOff,
        dateCreated = this.dateCreated,
        sumDayPeriodeBill = this.sumDayPeriodeBill,
        prorataDetail = this.prorataDetail
    )
}

fun Tagihan.toResultTagihan(): ResultTagihan{
    return ResultTagihan(
        idTagihan = this.idTagihan,
        statusTagihan = this.paymentStatus,
        periodStart = this.periodStart.toDayMonthShortAndYear(),
        periodEnd = this.periodEnd.toDayMonthShortAndYear(),
        jumlahDibayar = this.billAmount,
        nomorKamar = this.numberRoom,
        alasanPenolakan = this.rejectionStatement?: ""
    )
}