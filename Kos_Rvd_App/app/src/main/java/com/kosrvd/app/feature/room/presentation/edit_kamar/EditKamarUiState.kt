package com.kosrvd.app.feature.room.presentation.edit_kamar

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.core.domain.models.AlatElektronik
import com.kosrvd.app.feature.room.domain.utils.TypeEditKamar

data class EditKamarUiState(
    val isLoadingTarifKamar: Boolean = true,
    val idKamar: String = "",
    val typeEditKamar: TypeEditKamar? = null,
    val isButtonLoading: Boolean = false,
    val nomorKamarConstant: String = "",
    val numberRoom: String = "",
    val numberRoomError: UiText? = null,
    val isnumberRoomError: Boolean = false,
    val ukuranKamar: String? = "",
    val enableEditCapacity: Boolean = true,
    val oldJumlahOrang: Int = 1,
    val jumlahOrang: Int = 1,
    val tarifSatuOrang: String = "",
    val tarifSatuOrangError: UiText? = null,
    val isTarifSatuOrangError: Boolean = false,
    val tarifDuaOrang: String? = "",
    val tarifDuaOrangError: UiText? = null,
    val isTarifDuaOrangError: Boolean = false,
    val namaAlatElektronik: String = "",
    val layananElektronikGratisKamar: List<AlatElektronik> = emptyList(),
    val namaFasilitas: String = "",
    val fasilitasKamar: List<String> = emptyList(),
    val shakeTriggerNomorKamarError: Int = 0,
    val shakeTriggerTarifDuaOrangError: Int = 0,
    val shakeTriggerTarifSatuOrangError: Int = 0,
    val status: String = ""
)
