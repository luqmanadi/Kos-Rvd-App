package com.kosrvd.app.presentation.navigation.models

import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.InfoZonaParkir
import kotlinx.serialization.Serializable

@Serializable
data class ResultTagihan(
    val idTagihan: String = "",
    val statusTagihan: String = "",
    val periodStart: String = "",
    val periodEnd: String = "",
    val jumlahDibayar: Long = 0,
    val nomorKamar: String = "",
    val alasanPenolakan: String = ""
)

@Serializable
data class ResultLaporanKeluhan(
    val idLaporan: String = "",
    val statusLaporan: String = "",
    val nomorKamar: String = "",
    val namaPelapor: String = "",
    val judulLaporan: String = "",
)

@Serializable
data class ResultCreatePenyewaan(
    val idPenyewaan: String = "",
    val status: String = "",
    val nomorKamar: String = "",
    val penghuni: String = "",
    val totalTagihan: Long = 0
)

@Serializable
data class EditPindahKamarType(
    val idPenyewaan: String = "",
    val idKamar: String = "",
    val nomorKamar: String = "",
    val namePenghuniPertama: String = "",
    val roomCapacity: Int,
    val hargaSewaKamarSaatIni: HargaSewaKamar = HargaSewaKamar(0, null)
)

@Serializable
data class TambahPenghuniType(
    val idPenyewaan: String = "",
    val idKamar: String = "",
    val nomorKamar: String = "",
    val namePenghuni: List<String> = emptyList(),
    val roomCapacity: Int = 0,
    val hargaSewaKamarSaatIni: HargaSewaKamar = HargaSewaKamar(0, null)
)

@Serializable
data class HargaSewaKamar(
    val onePerson: Long,
    val twoPersons: Long?
)


@Serializable
data class TemporaryData(
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val address: String? = null
)

@Serializable
data class EditTypeKamar(
    val nomorKamar: String? = null,
    val ukuranKamar: String? = null,
    val tarifKamar: HargaSewaKamar = HargaSewaKamar(0, null),
    val layananElektronikKamar: List<AlatElektronikSerialize>? = null,
    val fasilitasKamar: List<String>? = null,
    val statusKamar: String? = null
)

@Serializable
data class AlatElektronikSerialize(
    val toolName: String,
    val cost: Long,
    val origin: String
)

@Serializable
data class InfoPakaiParkirMobilBulananSerialize(
    val carName: String,
    val numberPlate: String,
    val carBrand: String,
    val notes: String?,
    val zonaParkir: InfoZonaParkirSerialize
)

@Serializable
data class InfoZonaParkirSerialize(
    val idZonaParkir: String,
    val zoneName: String,
    val monthlyFee: Long
)

fun InfoZonaParkirSerialize.toInfoZonaParkir(): InfoZonaParkir {
    return InfoZonaParkir(
        idZonaParkir = this.idZonaParkir,
        zoneName = this.zoneName,
        monthlyFee = this.monthlyFee
    )
}

fun InfoPakaiParkirMobilBulanan.toInfoPakaiParkirMobilBulananSerialize(): InfoPakaiParkirMobilBulananSerialize {
    return InfoPakaiParkirMobilBulananSerialize(
        carName = this.carName,
        numberPlate = this.numberPlate,
        carBrand = this.carBrand,
        notes = this.notes,
        zonaParkir = this.zonaParkir.toInfoZonaParkirSerialize()
    )
}

fun InfoZonaParkir.toInfoZonaParkirSerialize(): InfoZonaParkirSerialize {
    return InfoZonaParkirSerialize(
        idZonaParkir = this.idZonaParkir,
        zoneName = this.zoneName,
        monthlyFee = this.monthlyFee
    )
}