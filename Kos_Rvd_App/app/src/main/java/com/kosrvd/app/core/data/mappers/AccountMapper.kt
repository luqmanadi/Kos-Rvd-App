package com.kosrvd.app.core.data.mappers

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.repository.dto.AccountDto
import com.kosrvd.app.core.data.repository.dto.DataPenghuniAkunPenggunaDto
import com.kosrvd.app.core.data.repository.dto.DetailAkunPenggunaDto
import com.kosrvd.app.core.data.repository.dto.DetailPenghuniDto
import com.kosrvd.app.core.data.repository.dto.FcmTokenDataDto
import com.kosrvd.app.core.data.repository.dto.TagihanTerakhirDto
import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.domain.models.DataPenghuniAkunPengguna
import com.kosrvd.app.core.domain.models.DetailAkunPengguna
import com.kosrvd.app.core.domain.models.DetailPenghuni
import com.kosrvd.app.core.domain.models.FcmToken
import com.kosrvd.app.core.domain.models.TagihanTerakhir
import java.util.Date


fun AccountDto.toAccount(): Account {
    return Account(
        idAkun = this.idAkun,
        name = this.name,
        photo = this.photo,
        role = this.role,
        status = this.status,
        dateCreated = this.dateCreated,
        fcmTokens = this.fcmTokens.toListFcmTokenData(),
        numberOfUnreadNotification = this.numberOfUnreadNotification,
        dataPenghuni = this.dataPenghuni?.toDetailPenghuni()
    )
}


fun DetailPenghuniDto.toDetailPenghuni(): DetailPenghuni {
    return DetailPenghuni(
        address = this.address,
        phoneNumber = this.phoneNumber,
        photoKtp = this.photoKtp,
        numberRoom = this.numberRoom,
        idPenyewa = this.idPenyewa,
        finalBill = this.finalBill?.toTagihanTerakhir(),
    )
}


fun TagihanTerakhirDto.toTagihanTerakhir(): TagihanTerakhir {
    return TagihanTerakhir(
        idTagihan = this.idTagihan,
        total = this.total,
        paymentStatus = this.paymentStatus,
        periodStart = this.periodStart,
        periodEnd = this.periodEnd,
        dueDate = this.dueDate,
        numberRoom = this.numberRoom
    )
}


fun List<AccountDto>.toListAccount(): List<Account>{
    return this.map { it.toAccount() }
}


fun List<FcmTokenDataDto>.toListFcmTokenData(): List<FcmToken> {
   return this.map { it.toFcmTokenData() }
}

fun FcmToken.toFcmTokenDataDto(): FcmTokenDataDto {
    return FcmTokenDataDto(
        token = this.token,
        deviceId = this.deviceId,
        deviceName = this.deviceName,
        platform = this.platform,
        lastUpdated = this.lastUpdated
    )
}

fun FcmTokenDataDto.toFcmTokenData(): FcmToken {
    return FcmToken(
        token = this.token,
        deviceId = this.deviceId,
        deviceName = this.deviceName,
        platform = this.platform,
        lastUpdated = this.lastUpdated
    )
}


fun DetailAkunPenggunaDto.toDetailAkunPengguna(): DetailAkunPengguna {
    return DetailAkunPengguna(
        idAkun = this.idAkun,
        name = this.name,
        photo = this.photo,
        role = this.role,
        email = this.email,
        status = this.status,
        dataPenghuni = this.dataPenghuni?.toDataPenghuniAkunPengguna(),
        dateCreated = Timestamp(Date(this.dateCreated))
    )
}

fun DataPenghuniAkunPenggunaDto.toDataPenghuniAkunPengguna(): DataPenghuniAkunPengguna {
    return DataPenghuniAkunPengguna(
        address = this.address,
        phoneNumber = this.phoneNumber,
        photoKtp = this.photoKtp,
        numberRoom = this.numberRoom
    )
}