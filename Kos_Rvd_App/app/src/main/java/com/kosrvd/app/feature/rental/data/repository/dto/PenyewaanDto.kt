package com.kosrvd.app.feature.rental.data.repository.dto

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.kosrvd.app.core.data.repository.dto.AlatElektronikDto

@Keep
data class PenyewaanDto(
    @DocumentId
    val idPenyewa: String = "",
    val infoKamar: InfoKamarSewaDto = InfoKamarSewaDto(),
    val listResident: List<InfoPenghuniDto> = emptyList(),
    val pemakaianAlatElektronikBulanan: List<AlatElektronikDto> = emptyList(),
    val pemakaianParkirMobilBulanan: InfoPakaiParkirMobilBulananDto? = null,
    val rentalStartDate: Timestamp = Timestamp.now(),
    val rentalCompletionDate: Timestamp? = null,
    val rentalStatus: String = ""
)
