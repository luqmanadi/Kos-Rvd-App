package com.kosrvd.app.feature.management.presentation.designsystem.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.ui.text.TextRange

object ZonaParkirFormatter {

    // Mengubah string ke format yang diinginkan berupa "Zona ${input}"
    private const val FORMATTER = "Zona %s"

    fun format(input: String): String {
        if (input.isEmpty()) return ""
        return FORMATTER.format(input)
    }

    // Menghapus format yang sudah ditambahkan berupa "Zona "
    fun parseToOriginal(input: String): String {
        return input.replace(FORMATTER.toRegex(), "")
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
