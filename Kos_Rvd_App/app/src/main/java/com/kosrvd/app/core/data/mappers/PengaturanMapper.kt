package com.kosrvd.app.core.data.mappers

import com.kosrvd.app.core.data.repository.dto.OtomatisasiDto
import com.kosrvd.app.core.domain.models.Otomatisasi

fun OtomatisasiDto.toOtomatisasi(): Otomatisasi {
    return Otomatisasi(
        useAutoReminder = this.useAutoReminder,
        useGenerateOtomatis = this.useGenerateOtomatis
    )
}