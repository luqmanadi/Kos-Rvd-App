package com.kosrvd.app.feature.management.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.constant.ErrorMessages
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.map
import com.kosrvd.app.feature.management.data.mappers.toOtomatisasi
import com.kosrvd.app.feature.management.data.repository.dto.OtomatisasiDto
import com.kosrvd.app.feature.management.domain.model.Otomatisasi
import com.kosrvd.app.feature.management.domain.repository.PengaturanRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PengaturanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): PengaturanRepository {
    override suspend fun getPengaturanTagihan(): Result<Otomatisasi, DataError> {
        return safeCall {
            db.collection(Constant.PENGATURAN_COLLECTION)
                .document(Constant.OTOMATISASI_DOC)
                .get()
                .await()
                .toObject<OtomatisasiDto>() ?: throw Exception(ErrorMessages.OTOMATISASI_NOT_FOUND)
        }.map { it.toOtomatisasi() }
    }

    override suspend fun updateAutoReminderPaymentBill(useAutoReminder: Boolean): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENGATURAN_COLLECTION)
                .document(Constant.OTOMATISASI_DOC)
                .update(Constant.USE_AUTO_REMINDER_FIELD, useAutoReminder)
                .await()
        }
    }

    override suspend fun updateGenerateTagihanOtomatis(useGenerateOtomatis: Boolean): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENGATURAN_COLLECTION)
                .document(Constant.OTOMATISASI_DOC)
                .update(Constant.USE_GENERATE_OTOMATIS_FIELD, useGenerateOtomatis)
                .await()
        }
    }
}