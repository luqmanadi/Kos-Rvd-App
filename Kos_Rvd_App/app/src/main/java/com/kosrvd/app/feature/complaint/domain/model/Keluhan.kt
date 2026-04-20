package com.kosrvd.app.feature.complaint.domain.model

import com.google.firebase.Timestamp

data class Keluhan(
    val complaintStatus: String,
    val completionDate: Timestamp? = null,
    val description: String,
    val idKeluhan: String,
    val idAkun: String,
    val numberRoom: String? = null,
    val photoComplaint: String? = null,
    val photoResponse: String? = null,
    val processDate: Timestamp? = null,
    val reportDate: Timestamp,
    val reporterName: String,
    val response: String? = null,
    val title: String
)