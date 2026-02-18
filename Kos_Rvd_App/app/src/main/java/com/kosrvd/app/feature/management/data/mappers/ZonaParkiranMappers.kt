package com.kosrvd.app.feature.management.data.mappers

import com.kosrvd.app.feature.management.data.repository.dto.ZonaParkiranDto
import com.kosrvd.app.feature.management.domain.model.ZonaParkiran

fun ZonaParkiranDto.toZonaParkiran(): ZonaParkiran {
    return ZonaParkiran(
        idZonaParkir = this.idZonaParkir,
        zoneName = this.zoneName,
        monthlyFee = this.monthlyFee,
        dailyCosts = this.dailyCosts,
        status = this.status
    )
}

fun List<ZonaParkiranDto>.toZonaParkiranList(): List<ZonaParkiran> {
    return this.map { it.toZonaParkiran() }
}