package com.kosrvd.app.feature.management.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.management.domain.model.DataDashboardAdmin
import kotlinx.coroutines.flow.Flow

interface RingkasanDashboardAdminRepository {
    fun getRingkasanDashboardAdmin(): Flow<Result<DataDashboardAdmin, DataError>>
}