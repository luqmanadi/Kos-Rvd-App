package com.kosrvd.app.feature.complaint.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.complaint.domain.model.Keluhan
import com.kosrvd.app.feature.complaint.domain.repository.KeluhanRepository
import com.kosrvd.app.feature.complaint.domain.utils.TypeKeluhan
import com.kosrvd.app.core.presentation.utils.toFormattedIndonesianDate
import com.kosrvd.app.feature.complaint.presentation.models.KeluhanGroupUi
import com.kosrvd.app.feature.complaint.presentation.models.ListKeluhanUi
import com.kosrvd.app.feature.complaint.presentation.models.toListKeluhanUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetListKeluhanUseCase @Inject constructor(
    private val keluhanRepository: KeluhanRepository,
    private val sessionStorage: SessionStorage
) {
    operator fun invoke(typeKeluhan: TypeKeluhan): Flow<Result<List<KeluhanGroupUi>, DataError>> =
        flow {
            val session = sessionStorage.getAuthInfo()
            when (session.role) {
                Role.ADMIN -> {
                    emitAll(
                        keluhanRepository.getAllKeluhanForAdmin(typeKeluhan)
                            .map { result ->
                                mapToListKeluhanGroupUi(result, typeKeluhan)
                            }
                    )
                }

                Role.PENGHUNI -> {
                    emitAll(
                        keluhanRepository.getAllKeluhanForPenghuni(typeKeluhan, session.idAkun)
                            .map { result ->
                                mapToListKeluhanGroupUi(result, typeKeluhan)
                            }
                    )
                }

                Role.EMPTY -> {
                    emit(Result.Error(DataError.LOCAL_DATA_ROLE_EMPTY))
                }
            }
        }

    private fun mapToListKeluhanGroupUi(
        result: Result<List<Keluhan>, DataError>,
        typeKeluhan: TypeKeluhan
    ): Result<List<KeluhanGroupUi>, DataError> {
        return when (result) {
            is Result.Success -> {
                val listKeluhan = result.data.map { it.toListKeluhanUi() }
                val groupedData = groupKeluhanByDayMonthAndYear(listKeluhan, typeKeluhan)
                Result.Success(groupedData)
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
    private fun groupKeluhanByDayMonthAndYear(
        listKeluhan: List<ListKeluhanUi>,
        status: TypeKeluhan
    ): List<KeluhanGroupUi> {
        // Mengelompokkan keluhan berdasarkan status dengan return tanggal sesuai dengan status
        val groupingDate = getGroupingDate(status)

        return listKeluhan.groupBy { keluhanList ->
            groupingDate(keluhanList)?.toFormattedIndonesianDate()
        }.map { (dateTitleGroup, keluhanList) ->
            val dateGroupTimestamp = groupingDate(keluhanList.first())
            KeluhanGroupUi(
                dateTitleGroup = dateTitleGroup ?: "",
                groupDate = dateGroupTimestamp?.toDate() ?: Timestamp.now().toDate(),
                items = keluhanList
            )
        }
    }

    private fun getGroupingDate(status: TypeKeluhan): (ListKeluhanUi) -> Timestamp? {
        return when(status){
            TypeKeluhan.WAITING_CONFIRMATION -> { it -> it.reportDate }
            TypeKeluhan.PROCESS -> { it -> it.processDate }
            TypeKeluhan.COMPLETION -> { it -> it.completionDate }
        }
    }
}