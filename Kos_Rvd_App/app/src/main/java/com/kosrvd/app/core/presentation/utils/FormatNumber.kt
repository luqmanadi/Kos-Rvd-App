package com.kosrvd.app.core.presentation.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun Long.toRupiahFormat(): String {
    val localeID = Locale.forLanguageTag("id-ID")
    val symbols = DecimalFormatSymbols(localeID)
    symbols.currencySymbol = "Rp "

    val formatter = DecimalFormat.getCurrencyInstance(localeID) as DecimalFormat
    formatter.decimalFormatSymbols = symbols
    formatter.maximumFractionDigits = 0

    return formatter.format(this)
}

fun String?.toNumberRoomFormat(): String = if(!this.isNullOrBlank() ) "Kamar No $this" else "Belum menempati kamar"

fun String?.toRoomFormat(): String = if(!this.isNullOrBlank()) "Kamar $this" else "Belum menempati kamar"

fun String?.toNumber(): String = if(!this.isNullOrBlank()) "No $this" else "Belum menempati kamar"

fun String?.toOnlyNumber(): String = if (!this.isNullOrBlank()) "$this" else "Belum menempati kamar"

fun Int.toCapacityPeople(): String = if (this != 0) "$this Orang" else "Belum Diatur"


