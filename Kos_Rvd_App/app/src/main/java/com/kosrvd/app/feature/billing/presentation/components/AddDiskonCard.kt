package com.kosrvd.app.feature.billing.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.billing.presentation.buat_tagihan.BuatTagihanUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddDiskonCard(
    modifier: Modifier = Modifier,
    percentageDiscountState: TextFieldState,
    descriptionDiscountState: TextFieldState,
    buatTagihanUiState: BuatTagihanUiState,
    percentageDiscountRequester: BringIntoViewRequester,
    percentageDiscountFocus: FocusRequester,
    descriptionDiscountRequester: BringIntoViewRequester,
    descriptionDiscountFocus: FocusRequester
) {
    val coroutineScope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.percent_discount),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Diskon logo"
            )
            Text(
                text = "Isi Tambah Potongan/Diskon",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(10.dp))
        GeneralTextField(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .bringIntoViewRequester(percentageDiscountRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            percentageDiscountRequester.bringIntoView()
                        }
                    }
                },
            focusRequester = percentageDiscountFocus,
            state = percentageDiscountState,
            isError = buatTagihanUiState.isPercentageDiscountError,
            error = buatTagihanUiState.percentageDiscountError?.asString() ?: "",
            label = R.string.masukkan_jumlah_diskon,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Percent,
                    contentDescription = "Diskon"
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            ),
            lineLimits = TextFieldLineLimits.SingleLine
        )
        GeneralTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .bringIntoViewRequester(descriptionDiscountRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            descriptionDiscountRequester.bringIntoView()
                        }
                    }
                },
            focusRequester = descriptionDiscountFocus,
            state = descriptionDiscountState,
            isError = buatTagihanUiState.isDescriptionDiscountError,
            error = buatTagihanUiState.descriptionDiscountError?.asString() ?: "",
            label = R.string.enter_information_discount,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            lineLimits = TextFieldLineLimits.SingleLine
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddDiskonCardPreview() {
    val percentageDiscountRequester = remember { BringIntoViewRequester() }
    val percentageDiscountFocus = remember { FocusRequester() }
    val descriptionDiscountRequester = remember { BringIntoViewRequester() }
    val descriptionDiscountFocus = remember { FocusRequester() }

    KosRvdAppTheme {
        AddDiskonCard(
            modifier = Modifier.padding(20.dp),
            percentageDiscountState = rememberTextFieldState(),
            descriptionDiscountState = rememberTextFieldState(),
            buatTagihanUiState = BuatTagihanUiState(),
            percentageDiscountRequester = percentageDiscountRequester,
            percentageDiscountFocus = percentageDiscountFocus,
            descriptionDiscountRequester = descriptionDiscountRequester,
            descriptionDiscountFocus = descriptionDiscountFocus
        )
    }
}