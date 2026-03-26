package com.kosrvd.app.core.presentation.designsystem.component.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.google.firebase.Timestamp
import com.kosrvd.app.core.presentation.utils.toFullIndonesianDateTime

@Composable
fun PaymentDeadlineText(
    modifier: Modifier = Modifier,
    deadline: Timestamp
) {
    // 1. Panggil fungsi formatter yang sudah kita buat
    val formattedDeadline = deadline.toFullIndonesianDateTime()

    // 2. Bangun string dengan gaya yang berbeda
    val styledText = buildAnnotatedString {
        // Terapkan gaya Bold untuk bagian pertama
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Transfer sebelum ")
        }
        // Tambahkan sisa teks dengan gaya normal
        append(formattedDeadline)
    }

    // 3. Tampilkan di dalam satu komponen Text
    Text(
        modifier = modifier,
        text = styledText,
        style = MaterialTheme.typography.bodySmall
    )
}