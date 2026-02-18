package com.kosrvd.app.feature.management.domain.model

import com.google.firebase.Timestamp

data class Pengumuman(
    val idPengumuman: String,
    val createdById: String,
    val madeBy: String,
    val title: String,
    val content: String,
    val dateCreated: Timestamp,
)
