package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.OtomatisasiDto
import com.kosrvd.app.feature.management.domain.model.Otomatisasi

fun OtomatisasiDto.toOtomatisasi(): Otomatisasi {
    return Otomatisasi(
        useAutoReminder = this.useAutoReminder,
        useGenerateOtomatis = this.useGenerateOtomatis
    )
}