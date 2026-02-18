package com.kosrvd.app.feature.management.presentation.ui.models

import com.google.firebase.Timestamp
import com.kosrvd.app.feature.management.domain.model.Tagihan
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toNumberRoomFormat
import com.kosrvd.app.feature.management.presentation.designsystem.utils.toRupiahFormat
import java.util.Date

data class ListTagihanUi(
    val idTagihan: String,
    val idPenyewa: String,
    val numberRoom: String,
    val residentNameList: List<String>,
    val billAmount: String,
    val paymentStatus: String,
    val dateCreated: Timestamp,
    val datePaidOff: Timestamp? = null,
    val dateUploadProof: Timestamp? = null
)


data class TagihanGroupUi(
    val dateTitleGroup: String,
    val groupDate: Date,
    val items: List<ListTagihanUi>
)


// Mapper Tagihan to ListTagihanUi
fun Tagihan.toListTagihanUi(): ListTagihanUi {
    return ListTagihanUi(
        idTagihan = this.idTagihan,
        idPenyewa = this.idPenyewa,
        numberRoom = this.numberRoom.toNumberRoomFormat(),
        residentNameList = this.residentNameList,
        billAmount = this.billAmount.toRupiahFormat(),
        paymentStatus = this.paymentStatus,
        dateCreated = this.dateCreated,
        datePaidOff = this.datePaidOff,
        dateUploadProof = this.dateUploadProof
    )
}