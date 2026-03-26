package com.kosrvd.app.feature.rental.presentation.edit_pemakaian_elektronik

import com.kosrvd.app.core.presentation.utils.UiText
import com.kosrvd.app.core.domain.models.AlatElektronik

data class EditPemakaianElektronikUiState(
    val idPenyewaan: String = "",
    val isButtonLoading: Boolean = false,
    val isButtonSubmitEnabled: Boolean = false,
    val isButtonAddAlatElektronikEnabled: Boolean = false,
    val namaAlatElektronik: String = "",
    val isNamaAlatElektronikError: Boolean = false,
    val namaAlatElektronikError: UiText? = null,
    val priceAlatElektronik: String = "",
    val isPriceAlatElektronikError: Boolean = false,
    val priceAlatElektronikError: UiText? = null,
    val listAlatElektronik: List<AlatElektronik> = emptyList(),
    val oldListAlatElektronik: List<AlatElektronik> = emptyList(),
)
