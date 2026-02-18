package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.core.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.feature.management.data.repository.dto.KeluhanDto
import com.kosrvd.app.feature.management.domain.model.BuatLaporanKeluhan
import com.kosrvd.app.feature.management.domain.model.Keluhan

fun KeluhanDto.toKeluhan(): Keluhan {
    return Keluhan(
        complaintStatus = this.complaintStatus,
        completionDate = this.completionDate,
        description = this.description,
        idKeluhan = this.idKeluhan,
        idAkun = this.idAkun,
        numberRoom = this.numberRoom,
        photoComplaint = this.photoComplaint,
        photoResponse = this.photoResponse,
        processDate = this.processDate,
        reportDate = this.reportDate,
        reporterName = this.reporterName,
        response = this.response,
        title = this.title
    )
}


fun BuatLaporanKeluhan.toKeluhan(idKeluhan: String): Keluhan {
    return Keluhan(
        complaintStatus = this.complaintStatus,
        completionDate = this.completionDate,
        description = this.description,
        idKeluhan = idKeluhan,
        idAkun = this.idAkun,
        numberRoom = this.numberRoom,
        photoComplaint = this.photoComplaint,
        photoResponse = this.photoResponse,
        processDate = this.processDate,
        reportDate = this.reportDate,
        reporterName = this.reporterName,
        response = this.response,
        title = this.title
    )
}

fun Keluhan.toResultLaporanKeluhan(): ResultLaporanKeluhan {
    return ResultLaporanKeluhan(
        idLaporan = this.idKeluhan,
        statusLaporan = this.complaintStatus,
        nomorKamar = this.numberRoom?: 0,
        namaPelapor = this.reporterName,
        judulLaporan = this.title,
    )
}