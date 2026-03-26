package com.kosrvd.app.core.presentation.utils

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class TypeResult {
    BUAT_KELUHAN,
    BAYAR_TAGIHAN,
    BAYAR_TAGIHAN_LANGSUNG_LUNAS,
    BERHASIL_VERIFIKASI_PEMBAYARAN_TAGIHAN,
    MENOLAK_PEMBAYARAN_TAGIHAN,
    PEMBUATAN_TAGIHAN,
    KONFIRMASI_SEKALIGUS_MEMPROSES_LAPORAN_KELUHAN,
    LAPORAN_KELUHAN_SELESAI_DIPROSES,
    BUAT_PENYEWAAN
}