package com.kosrvd.app.feature.management.domain.model

import com.google.firebase.Timestamp

data class BuatLaporanKeluhan(
    val complaintStatus: String,
    val completionDate: Timestamp? = null,
    val description: String,
    val idAkun: String,
    val numberRoom: Int? = null,
    val photoComplaint: String? = null ,
    val photoResponse: String? = null,
    val processDate: Timestamp? = null,
    val reportDate: Timestamp,
    val reporterName: String,
    val response: String? = null,
    val title: String
)