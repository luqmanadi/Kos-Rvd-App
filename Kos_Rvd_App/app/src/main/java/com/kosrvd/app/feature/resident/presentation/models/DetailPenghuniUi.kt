package com.kosrvd.app.feature.resident.presentation.models

import com.kosrvd.app.core.domain.models.Account
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear
import com.kosrvd.app.core.presentation.utils.toOnlyNumber

data class DetailPenghuniUi(
    val idPenghuni: String,
    val name: String,
    val numberRoom: String,
    val address: String,
    val phoneNumber: String,
    val photo: String,
    val status: String,
    val dateJoin: String,
)

fun Account.toDetailPenghuniUi(): DetailPenghuniUi{
    return DetailPenghuniUi(
        idPenghuni = this.idAkun,
        name = this.name,
        numberRoom = this.dataPenghuni?.numberRoom?.toOnlyNumber() ?: "Tidak Menempati Kamar",
        address = this.dataPenghuni?.address?: "Belum ada alamat",
        phoneNumber = this.dataPenghuni?.phoneNumber ?: "Belum ada nomor telepon",
        photo = this.photo,
        status = this.status,
        dateJoin = this.dateCreated.toDayMonthAndYear()
    )
}