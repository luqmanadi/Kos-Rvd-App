package com.kosrvd.app.core.domain.models

import com.google.firebase.Timestamp

data class Account(
    val idAkun: String,
    val name: String,
    val photo: String,
    val role: String,
    val status: String,
    val numberOfUnreadNotification: Int,
    val dataPenghuni: DetailPenghuni? = null,
    val dateCreated: Timestamp,
    val fcmTokens: List<FcmToken> = emptyList()
)

data class DetailAkunPengguna(
    val idAkun: String,
    val name: String,
    val photo: String,
    val role: String,
    val email: String,
    val status: String,
    val dataPenghuni: DataPenghuniAkunPengguna?,
    val dateCreated: Timestamp
)

data class DataPenghuniAkunPengguna(
    val address: String,
    val phoneNumber: String,
    val photoKtp: String,
    val numberRoom: Int?,
)

data class DetailPenghuni(
    val address: String,
    val phoneNumber: String,
    val photoKtp: String,
    val numberRoom: Int? = null,
    val finalBill: TagihanTerakhir? = null,
)

data class TagihanTerakhir(
    val idTagihan: String,
    val idPenyewa: String,
    val total: Long,
    val paymentStatus: String,
    val periodStart: Timestamp,
    val periodEnd: Timestamp,
    val dueDate: Timestamp
)