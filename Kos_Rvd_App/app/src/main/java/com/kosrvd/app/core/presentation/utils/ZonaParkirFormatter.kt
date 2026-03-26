package com.kosrvd.app.core.presentation.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.TextRange

object ZonaParkirFormatter {

    // Gunakan string literal biasa sebagai prefix
    private const val PREFIX = "Zona "

    fun format(input: String): String {
        if (input.isEmpty()) return ""

        // Pencegahan ganda: Jika entah kenapa input sudah memiliki prefix, kembalikan apa adanya
        if (input.startsWith(PREFIX)) return input

        return "$PREFIX$input"
    }

    // Menghapus format "Zona " di awal kata
    fun parseToOriginal(input: String): String {
        // removePrefix akan menghapus "Zona " HANYA jika kata itu ada di paling depan.
        return input.removePrefix(PREFIX)
    }
}

object ZonaParkirTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val rawText = ZonaParkirFormatter.parseToOriginal(this.asCharSequence().toString())

        if (rawText.isEmpty()) {
            this.replace(0, this.length, "")
            return
        }

        val formattedText = ZonaParkirFormatter.format(rawText)
        this.replace(0, this.length, formattedText)

        this.selection = TextRange(formattedText.length)
    }

}
