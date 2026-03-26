package com.kosrvd.app.core.presentation.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.TextRange
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object RupiahFormatter {
    private val symbols = DecimalFormatSymbols(INDONESIAN_LOCALE)
    private val formatter = DecimalFormat("Rp #,###", symbols)

    // Fungsi 1: Ubah angka murni (String/Long) jadi format Rupiah
    fun format(input: String): String {
        if (input.isEmpty()) return ""
        val parsed = input.filter { it.isDigit() }.toLongOrNull() ?: 0L
        if (parsed == 0L) return "" // Opsional: tampilkan kosong jika 0
        return formatter.format(parsed)
    }

    // Fungsi 2: Ubah format Rupiah jadi angka murni string ("Rp 1.000" -> "1000")
    fun parseToRaw(input: String): String {
        return input.filter { it.isDigit() }
    }
}

object RupiahInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val rawText = RupiahFormatter.parseToRaw(this.asCharSequence().toString())

        if (rawText.isEmpty()) {
            this.replace(0, this.length, "")
            return
        }

        val formattedText = RupiahFormatter.format(rawText)
        this.replace(0, this.length, formattedText)

        // Letakkan kursor di akhir
        this.selection = TextRange(formattedText.length)
    }
}