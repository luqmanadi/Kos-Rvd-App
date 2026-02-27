package com.kosrvd.app.feature.management.domain.usecase

import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.feature.management.domain.model.BillCalculationResult
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.roundToLong


class CalculateTotalBillUseCase @Inject constructor() {
    operator fun invoke(
        totalMonthlyBill: Long,
        adminFees: Boolean,
        periodStart: Long,
        periodEnd: Long,
        useDiscount: Boolean,
        percentageDiscount: String,
    ): BillCalculationResult {
        // 1. Hitung total hari ditempati (inklusif, ditambah 1)
        val totalHariDitempati = calculateTotalDays(periodStart, periodEnd)

        // 2. Hitung total hari dalam 1 periode baku (tgl 16 bulan lalu s.d 15 bulan ini)
        val totalHariDalamSatuPeriode = calculateDaysInPeriod(periodEnd)

        // 3. Kalkulasi Prorata
        // Rumus: (Total Hari Ditempati / Total Hari 1 Periode) * Total Biaya Normal
        val proratedBill = (totalHariDitempati.toDouble() / totalHariDalamSatuPeriode) * totalMonthlyBill

        // 4. Potong Diskon (Jika Ada)
        var discountAmount = 0.0
        var billAfterDiscount = proratedBill
        if (useDiscount && percentageDiscount.isNotBlank()) {
            val discountPercent = percentageDiscount.toDoubleOrNull() ?: 0.0
            discountAmount = proratedBill * (discountPercent / 100.0)
            billAfterDiscount = proratedBill - discountAmount
        }

        // 5. Tambahkan Biaya Admin (Jika Ada)
        // Asumsi nilai Constant.ADMIN_FEES adalah Long, misal 50000L
        val finalBill = if (adminFees) {
            billAfterDiscount + Constant.ADMIN_FEES
        } else {
            billAfterDiscount
        }

        // Return dalam bentuk Long (Dibulatkan agar tidak ada desimal)
        return BillCalculationResult(
            finalBill = finalBill.roundToLong(),
            priceDiscount = discountAmount.roundToLong()
        )
    }

    /**
     * Helper: Menghitung selisih hari antara dua waktu (Inklusif)
     */
    private fun calculateTotalDays(startMillis: Long, endMillis: Long): Int {
        val diffMillis = endMillis - startMillis
        // Dibagi dengan jumlah milidetik dalam 1 hari, ditambah 1 agar inklusif
        return (diffMillis / (1000 * 60 * 60 * 24)).toInt() + 1
    }

    /**
     * Helper: Menghitung total hari dari tgl 16 bulan sebelumnya sampai tgl 15 bulan dari periodEnd
     */
    private fun calculateDaysInPeriod(periodEndMillis: Long): Int {
        // Ambil bulan & tahun dari periodEnd
        val endCalendar = Calendar.getInstance().apply {
            timeInMillis = periodEndMillis
        }
        val endYear = endCalendar.get(Calendar.YEAR)
        val endMonth = endCalendar.get(Calendar.MONTH) // 0-indexed (Jan = 0)

        // Set Start Period (Tgl 16, Bulan Sebelumnya)
        val startPeriodCal = Calendar.getInstance().apply {
            set(endYear, endMonth - 1, 16, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Set End Period (Tgl 15, Bulan Saat Ini)
        val endPeriodCal = Calendar.getInstance().apply {
            set(endYear, endMonth, 15, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return calculateTotalDays(startPeriodCal.timeInMillis, endPeriodCal.timeInMillis)
    }
}