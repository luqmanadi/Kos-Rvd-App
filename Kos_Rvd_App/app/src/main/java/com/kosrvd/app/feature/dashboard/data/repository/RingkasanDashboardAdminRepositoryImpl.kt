package com.kosrvd.app.feature.dashboard.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.asFlowFirstItem
import com.kosrvd.app.feature.dashboard.data.repository.dto.DataDashboardAdminDto
import com.kosrvd.app.feature.dashboard.domain.repository.RingkasanDashboardAdminRepository
import com.kosrvd.app.feature.dashboard.data.mappers.toDataDashboardAdmin
import com.kosrvd.app.feature.dashboard.domain.model.DataDashboardAdmin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RingkasanDashboardAdminRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): RingkasanDashboardAdminRepository {
    override fun getRingkasanDashboardAdmin(): Flow<Result<DataDashboardAdmin, DataError>> {
        return db.collection(Constant.RINGKASAN_DASBORAD_ADMIN_COLLECTION)
            .limit(1)
            .asFlowFirstItem<DataDashboardAdminDto, DataDashboardAdmin>(
                mapper = { it.toDataDashboardAdmin() },
                onNotFound = { DataError.DATA_DASHBOARD_ADMIN_NOT_FOUND },
                onMappingError = { DataError.DATA_DASHBOARD_ADMIN_MAPPING_ERROR }
            )
    }
}