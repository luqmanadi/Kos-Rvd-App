package com.kosrvd.app.feature.rental.data.mappers

import com.kosrvd.app.core.data.repository.dto.HargaDto
import com.kosrvd.app.feature.rental.data.repository.dto.InfoKamarSewaDto
import com.kosrvd.app.feature.rental.data.repository.dto.InfoPakaiParkirMobilBulananDto
import com.kosrvd.app.feature.rental.data.repository.dto.InfoPenghuniDto
import com.kosrvd.app.feature.rental.data.repository.dto.InfoZonaParkirDto
import com.kosrvd.app.feature.rental.data.repository.dto.PenyewaanDto
import com.kosrvd.app.feature.rental.domain.model.BuatPenyewaan
import com.kosrvd.app.core.domain.models.Harga
import com.kosrvd.app.feature.rental.domain.model.InfoKamarSewa
import com.kosrvd.app.feature.rental.domain.model.InfoPakaiParkirMobilBulanan
import com.kosrvd.app.feature.rental.domain.model.InfoPenghuni
import com.kosrvd.app.feature.rental.domain.model.InfoZonaParkir
import com.kosrvd.app.feature.rental.domain.model.Penyewaan
import com.kosrvd.app.feature.billing.data.mappers.toAlatElektronik

fun PenyewaanDto.toPenyewaan(): Penyewaan{
    return Penyewaan(
        idPenyewa = this.idPenyewa,
        infoKamar = this.infoKamar.toInfoKamarSewa(),
        listResident = this.listResident.map { it.toInfoPenghuni() },
        pemakaianAlatElektronikBulanan = this.pemakaianAlatElektronikBulanan.map { it.toAlatElektronik() },
        pemakaianParkirMobilBulanan = this.pemakaianParkirMobilBulanan?.toInfoPakaiParkirMobil(),
        rentalStartDate = this.rentalStartDate,
        rentalCompletionDate = this.rentalCompletionDate,
        rentalStatus = this.rentalStatus
    )
}

fun InfoKamarSewaDto.toInfoKamarSewa(): InfoKamarSewa {
    return InfoKamarSewa(
        idKamar = this.idKamar,
        currentRoomRentalCost = this.currentRoomRentalCost.toHarga(),
        numberRoom = this.numberRoom
    )
}

fun InfoPakaiParkirMobilBulananDto.toInfoPakaiParkirMobil(): InfoPakaiParkirMobilBulanan {
    return InfoPakaiParkirMobilBulanan(
        carName = this.carName,
        numberPlate = this.numberPlate,
        carBrand = this.carBrand,
        notes = this.notes,
        zonaParkir = this.zonaParkir.toInfoZonaParkir()
    )
}

fun InfoZonaParkirDto.toInfoZonaParkir(): InfoZonaParkir {
    return InfoZonaParkir(
        idZonaParkir = this.idZonaParkir,
        zoneName = this.zoneName,
        monthlyFee = this.monthlyFee
    )
}

fun InfoPenghuniDto.toInfoPenghuni(): InfoPenghuni{
    return InfoPenghuni(
        idAkun = this.idAkun,
        name = this.name
    )
}
fun BuatPenyewaan.toPenyewaan(idPenyewaan: String): Penyewaan{
    return Penyewaan(
        idPenyewa = idPenyewaan,
        infoKamar = this.infoKamar,
        listResident = this.listResident,
        rentalStartDate = this.rentalStartDate,
        rentalCompletionDate = this.rentalCompletionDate,
        rentalStatus = this.rentalStatus,
        pemakaianAlatElektronikBulanan = this.pemakaianAlatElektronikBulanan,
        pemakaianParkirMobilBulanan = this.pemakaianParkirMobilBulanan
    )
}

fun HargaDto.toHarga(): Harga{
    return Harga(
        onePerson = this.onePerson,
        twoPersons = this.twoPersons
    )
}


fun List<PenyewaanDto>.toListPenyewaan(): List<Penyewaan> {
    return this.map { it.toPenyewaan() }
}