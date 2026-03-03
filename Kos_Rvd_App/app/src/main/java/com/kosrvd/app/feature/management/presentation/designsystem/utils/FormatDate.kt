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


fun getDueDateAsFifteenthOfMonth(originalTimestamp: Timestamp): Timestamp {
    // 1. Konversi Timestamp Firebase ke java.util.Date
    val originalDate = originalTimestamp.toDate()

    // 2. Gunakan Calendar untuk manipulasi tanggal
    val calendar = Calendar.getInstance()
    calendar.time = originalDate // Set Calendar ke tanggal asli

    // 3. Set hari dalam bulan menjadi 15
    calendar.set(Calendar.DAY_OF_MONTH, 15)

    // 4. Set waktu menjadi 23:59:59 (akhir hari)
    calendar.set(Calendar.HOUR_OF_DAY, 23) // Jam 23 (11 PM)
    calendar.set(Calendar.MINUTE, 59)     // Menit 59
    calendar.set(Calendar.SECOND, 59)     // Detik 59
    calendar.set(Calendar.MILLISECOND, 999) // Milidetik 999 (mewakili akhir detik ke-59, sebelum detik berikutnya)

    // 5. Konversi kembali dari Calendar ke java.util.Date
    val dueDateAsDate = calendar.time

    // 6. Konversi java.util.Date ke Timestamp Firebase dan kembalikan
    return Timestamp(dueDateAsDate)
}

fun getDueDateAsFifteenthOfMonth(): Timestamp {
    // Ambil waktu saat ini
    val now = Timestamp.now()
    return getDueDateAsFifteenthOfMonth(now)
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