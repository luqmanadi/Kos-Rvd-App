package com.kosrvd.app.feature.account.presentation.models

import com.kosrvd.app.core.domain.models.DataPenghuniAkunPengguna
import com.kosrvd.app.core.domain.models.DetailAkunPengguna
import com.kosrvd.app.core.presentation.utils.toDayMonthAndYear

data class DetailAkunUi(
    val idAkun: String,
    val name: String,
    val photo: String,
    val role: String,
    val email: String,
    val status: String,
    val dataPenghuni: DetailAkunPenghuni?,
    val dateCreated: String
)

data class DetailAkunPenghuni(
    val address: String,
    val phoneNumber: String,
    val photoKtp: String,
    val numberRoom: String?,
)

fun DetailAkunPengguna.toDetailAkunUi(): DetailAkunUi{
    return DetailAkunUi(
        idAkun = this.idAkun,
        name = this.name,
        photo = this.photo,
        role = this.role,
        email = this.email,
        status = this.status,
        dataPenghuni = this.dataPenghuni?.toDetailAkunPenghuni(),
        dateCreated = this.dateCreated.toDayMonthAndYear()
    )
}

fun DataPenghuniAkunPengguna.toDetailAkunPenghuni() : DetailAkunPenghuni {
    return DetailAkunPenghuni(
        address = this.address,
        phoneNumber = this.phoneNumber,
        photoKtp = this.photoKtp,
        numberRoom = this.numberRoom
    )
}