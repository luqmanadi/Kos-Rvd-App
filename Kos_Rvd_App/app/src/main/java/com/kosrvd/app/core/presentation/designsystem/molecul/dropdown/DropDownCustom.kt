package com.kosrvd.app.core.presentation.designsystem.molecul.dropdown

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownCustomV1(
    modifier: Modifier = Modifier,
    items: List<T?>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    itemToString: (T?) -> String,
    @StringRes textNoData: Int,
    itemEnabled: (T?) -> Boolean = { true }
) {
    // State untuk mengontrol apakah menu dropdown sedang ditampilkan atau tidak
    var isExpanded by remember { mutableStateOf(false) }

    // Box yang akan menangani state expanded dari dropdown
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { newExpandedState ->
            isExpanded = newExpandedState
        },
        modifier = modifier
    ) {
        // OutlinedTextField yang akan menampilkan nilai terpilih
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable) // Modifier ini wajib untuk menghubungkan TextField dengan menu
                .fillMaxWidth()
                .focusProperties {
                    canFocus = isExpanded
                },
            value = itemToString(selectedItem),
            onValueChange = {}, // Dibiarkan kosong karena nilai diubah melalui menu
            readOnly = true, // Membuat TextField tidak bisa diedit secara manual
            trailingIcon = {
                // Ikon panah yang akan berubah sesuai state expanded
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            shape = RoundedCornerShape(10.dp),
        )

        // Menu yang akan muncul di bawah TextField
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            } // Menutup menu saat area di luar diklik
        ) {
            // Membuat item untuk setiap string dalam 'items'
            if(items.isNotEmpty()){
                items.forEach { item ->
                    val isEnabled = item == null || itemEnabled(item)
                    DropdownMenuItem(
                        enabled = isEnabled,
                        modifier = Modifier.background(color = if (selectedItem == item) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceContainer),
                        trailingIcon = {
                            if (item == selectedItem){
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                )
                            }
                        },
                        text = { 
                            Text(
                                text = itemToString(item),
                                color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            ) 
                        },
                        onClick = {
                            onItemSelected(item) // Memanggil callback saat item dipilih
                            isExpanded = false // Menutup menu setelah item dipilih
                        },
                    )
                }
            } else {
                Text(
                    text = stringResource(textNoData),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropDownMenuV1Preview() {
    KosRvdAppTheme {
        // --- Contoh Penggunaan untuk Preview ---
        val items = listOf(
            null,
            PenghuniV1("1", "NdiM", "9"),
            PenghuniV1("2", "Fufu", "11"),
            PenghuniV1("3", "Juaja", "4"),
        )
        var selectedItem by remember { mutableStateOf<PenghuniV1?>(null) }

        // --- BUNGKUS DENGAN BOX ---
        Box(
            modifier = Modifier
                .fillMaxSize() // Memastikan Box mengisi seluruh area preview
                .padding(20.dp)
        ) {
            DropDownCustomV1(
                // modifier tidak perlu padding lagi karena sudah dihandle oleh Box
                items = items,
                selectedItem = selectedItem,
                itemToString = { penghuni ->
                    if (penghuni != null) {
                        "${penghuni.nama} - ${penghuni.nomorKamar}"
                    } else {
                        "Pilih seorang penghuni"
                    }
                },
                onItemSelected = { selectedItem = it },
                textNoData = R.string.no_data_rental
            )
        }
    }
}

private data class PenghuniV1(
    val id: String,
    val nama: String,
    val nomorKamar: String
)