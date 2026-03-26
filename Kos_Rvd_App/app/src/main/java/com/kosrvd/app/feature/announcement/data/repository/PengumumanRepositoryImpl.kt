package com.kosrvd.app.feature.announcement.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.data.networking.safeCall
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.asCollectionFlow
import com.kosrvd.app.feature.announcement.data.mappers.toPengumuman
import com.kosrvd.app.feature.announcement.data.repository.dto.PengumumanDto
import com.kosrvd.app.feature.announcement.domain.model.BuatPengumuman
import com.kosrvd.app.feature.announcement.domain.model.Pengumuman
import com.kosrvd.app.feature.announcement.domain.repository.PengumumanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PengumumanRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): PengumumanRepository {
    override suspend fun getAllPengumuman(): Flow<Result<List<Pengumuman>, DataError>> {
        return db.collection(Constant.PENGUMUMAN_COLLECTION)
            .orderBy(Constant.DATE_CREATED, Query.Direction.DESCENDING)
            .asCollectionFlow<PengumumanDto, Pengumuman>(
                logTag = "Pengumuman Repo",
                mapper = { it.toPengumuman() }
            )
    }

    override suspend fun deletePengumuman(idPengumuman: String): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENGUMUMAN_COLLECTION)
                .document(idPengumuman)
                .delete()
                .await()
        }
    }

    override suspend fun createPengumuman(createPengumuman: BuatPengumuman): Result<Unit, DataError> {
        return safeCall {
            db.collection(Constant.PENGUMUMAN_COLLECTION)
                .add(createPengumuman)
                .await()
        }
    }
}