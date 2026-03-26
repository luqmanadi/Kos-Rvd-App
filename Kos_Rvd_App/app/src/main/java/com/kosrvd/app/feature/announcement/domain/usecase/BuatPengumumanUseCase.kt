package com.kosrvd.app.feature.announcement.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.announcement.domain.model.BuatPengumuman
import com.kosrvd.app.feature.announcement.domain.repository.PengumumanRepository
import javax.inject.Inject

class BuatPengumumanUseCase @Inject constructor(
    private val pengumumanRepository: PengumumanRepository,
    private val sessionStorage: SessionStorage
) {
    suspend operator fun invoke(title: String, content: String): Result<Unit, DataError> {
        val idAkun = sessionStorage.getAuthInfo().idAkun
        val pengumuman = BuatPengumuman(
            title = title,
            content = content,
            createdById = idAkun,
            dateCreated = Timestamp.now(),
            madeBy = "Admin"
        )
        return pengumumanRepository.createPengumuman(pengumuman)
    }
}