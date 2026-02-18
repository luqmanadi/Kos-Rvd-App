package com.kosrvd.app.feature.management.presentation.designsystem.utils

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

fun Int.toNumberRoomFormat(): String = if(this != 0 ) "Kamar No $this" else "Belum menempati kamar"

fun Int.toRoomFormat(): String = if(this != 0) "Kamar $this" else "Belum menempati kamar"

fun Int.toNumber(): String = if(this != 0) "No $this" else "Belum menempati kamar"

fun Int.toOnlyNumber(): String = if (this != 0) "$this" else "Belum menempati kamar"

fun Int.toCapacityPeople(): String = if (this != 0) "$this Orang" else "Belum Diatur"


