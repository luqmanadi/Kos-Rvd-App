package com.kosrvd.app.feature.dashboard.domain.repository

import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.feature.dashboard.domain.model.DataDashboardAdmin
import kotlinx.coroutines.flow.Flow

interface RingkasanDashboardAdminRepository {
    fun getRingkasanDashboardAdmin(): Flow<Result<DataDashboardAdmin, DataError>>
}