package com.kosrvd.app.feature.billing.domain.usecase

import com.google.firebase.Timestamp
import com.kosrvd.app.core.data.source.local.SessionStorage
import com.kosrvd.app.core.domain.utils.DataError
import com.kosrvd.app.core.domain.utils.Result
import com.kosrvd.app.core.domain.utils.Role
import com.kosrvd.app.feature.billing.domain.utils.TypeTagihan
import com.kosrvd.app.core.presentation.utils.toMonthAndYear
import com.kosrvd.app.feature.billing.presentation.models.ListTagihanUi
import com.kosrvd.app.feature.billing.presentation.models.TagihanGroupUi
import com.kosrvd.app.feature.billing.presentation.models.toListTagihanUi
import com.kosrvd.app.feature.billing.domain.model.Tagihan
import com.kosrvd.app.feature.billing.domain.repository.TagihanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetListTagihanUseCase @Inject constructor(
    private val sessionStorage: SessionStorage,
    private val tagihanRepository: TagihanRepository
)  {

    operator fun invoke(typeTagihan: TypeTagihan): Flow<Result<List<TagihanGroupUi>, DataError>> =
        flow {
            val session = sessionStorage.getAuthInfo()
            when (session.role) {
                Role.ADMIN -> {
                    emitAll(
                        tagihanRepository.getAllTagihanForAdmin(typeTagihan)
                            .map { result ->
                                mapToListTagihanGroupUi(result, typeTagihan)
                            }
                    )
                }

                Role.PENGHUNI -> {
                    emitAll(
                        tagihanRepository.getAllTagihanForPenghuni(typeTagihan, session.idAkun)
                            .map { result ->
                                mapToListTagihanGroupUi(result, typeTagihan)
                            }
                    )

                }

                Role.EMPTY -> {
                    emit(Result.Error(DataError.LOCAL_DATA_ROLE_EMPTY))
                }
            }
        }

    private fun mapToListTagihanGroupUi(
        result: Result<List<Tagihan>, DataError>,
        typeTagihan: TypeTagihan
    ): Result<List<TagihanGroupUi>, DataError> {
        return when (result) {
            is Result.Success -> {
                val listTagihan = result.data.map { it.toListTagihanUi() }
                val groupedData = groupTagihanByMonth(listTagihan, typeTagihan)
                Result.Success(groupedData)
            }
            is Result.Error -> {
                Result.Error(result.error)
            }
        }
    }
    private fun groupTagihanByMonth(
        tagihanList: List<ListTagihanUi>,
        status: TypeTagihan
    ): List<TagihanGroupUi> {
        // Mengelompokkan tagihan berdasarkan status dengan return tanggal sesuai dengan status
        val groupingDate = getGroupingDate(status)

        return tagihanList.groupBy { listTagihanUi ->
            groupingDate(listTagihanUi)?.toMonthAndYear()
        }.map { (dateTitleGroup, tagihanList) ->
            val dateGroupTimestamp = groupingDate(tagihanList.first())
            TagihanGroupUi(
                dateTitleGroup = dateTitleGroup ?: "",
                groupDate = dateGroupTimestamp?.toDate() ?: Timestamp.now().toDate(),
                items = tagihanList
            )
        }
    }

    private fun getGroupingDate(status: TypeTagihan): (ListTagihanUi) -> Timestamp? {
        return when(status){
            TypeTagihan.PAID_OFF -> { it -> it.datePaidOff }
            TypeTagihan.WAITING_VERIFICATION -> { it -> it.dateUploadProof }
            TypeTagihan.UNPAID -> { it -> it.dateCreated }
        }
    }
}