package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.feature.management.data.repository.dto.AlatElektronikDto
import com.kosrvd.app.feature.management.data.repository.dto.TagihanDto
import com.kosrvd.app.feature.management.domain.model.AlatElektronik
import com.kosrvd.app.feature.management.domain.model.BuatTagihan
import com.kosrvd.app.feature.management.domain.model.Tagihan
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toMonthAndYear

fun TagihanDto.toTagihan(): Tagihan{
    return Tagihan(
        adminFees = this.adminFees,
        billAmount = this.billAmount,
        billingMonth = this.billingMonth,
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
        verificationDate = this.verificationDate
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
        billingMonth = this.billingMonth,
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
        dateCreated = this.dateCreated
    )
}

fun Tagihan.toResultTagihan(): ResultTagihan{
    return ResultTagihan(
        idTagihan = this.idTagihan,
        statusTagihan = this.paymentStatus,
        bulan = this.billingMonth.toMonthAndYear(),
        jumlahDibayar = this.billAmount,
        nomorKamar = this.numberRoom,
        alasanPenolakan = this.rejectionStatement?: ""
    )
}