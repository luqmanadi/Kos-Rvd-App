package com.kosrvd.app.feature.management.presentation.designsystem.utils

import android.icu.util.Calendar
import com.google.firebase.Timestamp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

val INDONESIAN_LOCALE: Locale = Locale.forLanguageTag("id-ID")
private val JAKARTA_ZONE_ID = ZoneId.of("Asia/Jakarta")
val UTC_ZONE_ID: ZoneId = ZoneId.of("UTC")

// Contoh output: "15 Juli 2025, 23:59 WIB"
fun Timestamp.toFullIndonesianDateTime(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "dd MMMM yyyy, HH:mm 'WIB.'"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)

    return formatter.format(instant)
}

// Contoh output: "15 Juli 2025, 23:59"
fun Timestamp.toAnnouncementDateTimeFormat(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "dd MMMM yyyy, HH:mm"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)

    return formatter.format(instant)
}

// contoh output: "15 Jul 2025, 23:59"
fun Timestamp.toAnnouncementDateTimeFormatShort(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "dd MMM yyyy, HH:mm"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)
    return formatter.format(instant)
}

// Contoh Output: "Bulan Juni"
fun Timestamp.toMonth(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "'Bulan' MMMM"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)

    return formatter.format(instant)
}

// Contoh Output: "Juni 2025"
fun Timestamp.toMonthAndYear(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "MMMM yyyy"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)

    return formatter.format(instant)
}

// Contoh Output: "15 Juli 2025"
fun Timestamp.toDayMonthAndYear(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "dd MMMM yyyy"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)
    return formatter.format(instant)
}

// Contoh Output: "Senin, 15 Juli 2025"
fun Timestamp.toFormattedIndonesianDate(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "EEEE, dd MMMM yyyy"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)
    return formatter.format(instant)
}

// Contoh Output: "16 Feb 2026"
fun Timestamp.toDayMonthShortAndYear(): String {
    val instant: Instant = this.toDate().toInstant()
    val pattern = "dd MMM yyyy"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(JAKARTA_ZONE_ID)
    return formatter.format(instant)
}


fun calculateSmartDueDate(periodStart: Timestamp): Timestamp {
    // Konversi ke LocalDate (Jakarta Zone agar akurat dengan kalender Indonesia)
    val startDate = periodStart.toDate().toInstant()
        .atZone(JAKARTA_ZONE_ID)
        .toLocalDate()

    val day = startDate.dayOfMonth

    val dueDateLocal = if (day == 16) {
        // SKEMA 1: Jika tgl 16, maka due date tgl 15 bulan tersebut
        startDate.withDayOfMonth(15)
    } else {
        // SKEMA 2: Jika bukan tgl 16, maka due date = Start Date + 2 hari (Total 3 hari)
        // Contoh: Masuk tgl 20, due date tgl 22.
        startDate.plusDays(2)
    }

    // Set ke jam 23:59:59 agar tidak dianggap telat di detik pertama hari tersebut
    val dueDateTime = dueDateLocal.atTime(23, 59, 59)
    val instant = dueDateTime.atZone(JAKARTA_ZONE_ID).toInstant()

    return Timestamp(instant.epochSecond, instant.nano)
}

// Contoh Output: "15 Jul 2025"
fun Long.toDayMonthShortAndYear(zoneId: ZoneId = JAKARTA_ZONE_ID): String {
    val instant: Instant = Instant.ofEpochMilli(this)
    val pattern = "dd MMM yyyy"
    val formatter = DateTimeFormatter
        .ofPattern(pattern, INDONESIAN_LOCALE)
        .withZone(zoneId)
    return formatter.format(instant)
}

// Set ke jam 00:00:00
fun Long.toStartOfDay(): Long {
    return Instant.ofEpochMilli(this)
        .atZone(UTC_ZONE_ID) // Ambil mentahan dari Picker
        .withZoneSameLocal(JAKARTA_ZONE_ID) // Paksa anggap ini waktu Jakarta tanpa geser jam
        .withHour(0)
        .withMinute(1)
        .withSecond(0)
        .toInstant()
        .toEpochMilli()
}

// Set ke jam 23:59:59
fun Long.toEndOfDay(): Long {
    return Instant.ofEpochMilli(this)
        .atZone(UTC_ZONE_ID)
        .withZoneSameLocal(JAKARTA_ZONE_ID)
        .withHour(23)
        .withMinute(59)
        .withSecond(59)
        .toInstant()
        .toEpochMilli()
}

/**
 * Mengambil waktu sekarang (Today) dengan jam diatur ke 23:59:59 zona Jakarta.
 */
fun getNowEndOfDay(): Timestamp {
    val instant = Instant.now()
        .atZone(JAKARTA_ZONE_ID)
        .withHour(23)
        .withMinute(59)
        .withSecond(59)
        .withNano(0)
        .toInstant()
    return Timestamp(instant.epochSecond, instant.nano)
}

/**
 * Mengubah Timestamp yang ada menjadi akhir hari (23:59:59) di zona Jakarta.
 */
fun Timestamp.toEndOfDay(): Timestamp {
    val instant = this.toDate().toInstant()
        .atZone(JAKARTA_ZONE_ID)
        .withHour(23)
        .withMinute(59)
        .withSecond(59)
        .withNano(0)
        .toInstant()
    return Timestamp(instant.epochSecond, instant.nano)
}
