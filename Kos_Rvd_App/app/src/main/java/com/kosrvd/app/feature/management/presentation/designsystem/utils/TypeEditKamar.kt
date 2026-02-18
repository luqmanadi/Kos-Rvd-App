package com.kosrvd.app.feature.management.presentation.designsystem.utils

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class TypeEditKamar {
    EDIT_NOMOR_KAMAR,
    EDIT_UKURAN_KAMAR,
    EDIT_TARIF_KAMAR,
    EDIT_LAYANAN_ALAT_ELEKTRONIK_GRATIS,
    EDIT_FASILITAS_KAMAR
}