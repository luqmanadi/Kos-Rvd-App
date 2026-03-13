package com.kosrvd.app.core.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

@Keep
data class AccountDto(
    @DocumentId
    val idAkun: String = "",
    val name: String = "",
    val photo: String = "",
    val role: String = "",
    val status: String = "",
    val numberOfUnreadNotification: Int = 0,
    val dateCreated: Timestamp = Timestamp.now(),
    val fcmTokens: List<FcmTokenDataDto> = emptyList(),
    val dataPenghuni: DetailPenghuniDto? = null
)

@Keep
data class DetailPenghuniDto(
    val numberRoom: Int? = null,
    val address: String = "",
    val phoneNumber: String = "",
    val photoKtp: String = "",
    val idPenyewa: String = "",
    val finalBill: TagihanTerakhirDto? = null
)


@Keep
data class TagihanTerakhirDto(
    val idTagihan: String = "",
    val periodStart: Timestamp = Timestamp.now(),
    val periodEnd: Timestamp = Timestamp.now(),
    val dueDate: Timestamp = Timestamp.now(),
    val total: Long = 0,
    val paymentStatus: String = ""
)


@Keep
@Serializable
data class DetailAkunPenggunaDto (
    val idAkun: String = "",
    val name: String = "",
    val photo: String = "",
    val role: String = "",
    val email: String = "",
    val status: String = "",
    val dataPenghuni: DataPenghuniAkunPenggunaDto? = null,
    val dateCreated: Long = 0L
)

@Keep
@Serializable
data class DataPenghuniAkunPenggunaDto(
    val address: String = "",
    val phoneNumber: String = "",
    val photoKtp: String = "",
    val numberRoom: Int? = null,
)
