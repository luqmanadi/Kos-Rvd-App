package com.kosrvd.app.feature.management.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.Otomatisasi

interface PengaturanRepository {
    suspend fun getPengaturanTagihan(): Result<Otomatisasi, DataError>
    suspend fun updateAutoReminderPaymentBill(useAutoReminder: Boolean): Result<Unit, DataError>
    suspend fun updateGenerateTagihanOtomatis(useGenerateOtomatis: Boolean): Result<Unit, DataError>
}