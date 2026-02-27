package com.kosrvd.app.feature.management.presentation.ui.screen.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.navigation.models.ResultCreatePenyewaan
import com.kosrvd.app.core.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionOutlineButton
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeResult
import com.kosrvd.app.feature.management.presentation.ui.screen.result.component.InfoResulTagihanCard
import com.kosrvd.app.feature.management.presentation.ui.screen.result.component.InfoResultBuatKeluhanCard
import com.kosrvd.app.feature.management.presentation.ui.screen.result.component.InfoResultCreatePenyewaanCard

@Composable
fun ResultScreen(
    typeResult: TypeResult,
    resultTagihan: ResultTagihan? = null,
    resultLaporanKeluhan: ResultLaporanKeluhan? = null,
    resultCreatePenyewaan: ResultCreatePenyewaan? = null,
    navigateToDetailPenyewaan: () -> Unit = {},
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold { innePadding ->
        when (typeResult) {
            TypeResult.BUAT_KELUHAN -> {
                ResultKeluhanContent(
                    modifier = Modifier.padding(innePadding),
                    resultLaporanKeluhan = resultLaporanKeluhan?: ResultLaporanKeluhan(),
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListKeluhan = navigateBack,
                    description = stringResource(R.string.description_success_make_a_complain),
                    title = stringResource(R.string.title_success_make_a_complain)
                )
            }
            TypeResult.BAYAR_TAGIHAN -> {
                ResultTagihanContent(
                    modifier = Modifier.padding(innePadding),
                    resultTagihan = resultTagihan?: ResultTagihan(),
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListTagihan = navigateBack,
                    description = stringResource(R.string.description_payment_success),
                    isCreateTagihan = false,
                    title = stringResource(R.string.title_payment_success),
                    subtitle = stringResource(R.string.subtitle_payment_success)
                )
            }
            TypeResult.BAYAR_TAGIHAN_LANGSUNG_LUNAS -> {
                ResultTagihanContent(
                    modifier = Modifier.padding(innePadding),
                    resultTagihan = resultTagihan?: ResultTagihan(),
                    description = stringResource(R.string.description_payment_bill_or_verification_success),
                    isCreateTagihan = false,
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListTagihan = navigateBack,
                    title = stringResource(R.string.title_payment_bill_success),
                    subtitle = stringResource(R.string.subtitle_payment_bill_success)
                )
            }
            TypeResult.BERHASIL_VERIFIKASI_PEMBAYARAN_TAGIHAN -> {
                ResultTagihanContent(
                    modifier = Modifier.padding(innePadding),
                    resultTagihan = resultTagihan?: ResultTagihan(),
                    description = stringResource(R.string.description_payment_bill_or_verification_success),
                    isCreateTagihan = false,
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListTagihan = navigateBack,
                    title = stringResource(R.string.title_verification_success),
                    subtitle = stringResource(R.string.subtitle_verification_success)
                )
            }
            TypeResult.MENOLAK_PEMBAYARAN_TAGIHAN -> {
                ResultTagihanContent(
                    modifier = Modifier.padding(innePadding),
                    resultTagihan = resultTagihan?: ResultTagihan(),
                    title = stringResource(R.string.title_rejected_payment),
                    subtitle = stringResource(R.string.subtitle_rejected_payment),
                    isCreateTagihan = false,
                    isTitleRedColor = true,
                    icon = R.drawable.ic_rejected,
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListTagihan = navigateBack
                )
            }
            TypeResult.PEMBUATAN_TAGIHAN -> {
                ResultTagihanContent(
                    modifier = Modifier.padding(innePadding),
                    resultTagihan = resultTagihan?: ResultTagihan(),
                    title = stringResource(R.string.title_success_make_a_new_bill),
                    subtitle = stringResource(R.string.subtitle_success_make_a_new_bill),
                    description = stringResource(R.string.description_success_make_a_new_bill),
                    isCreateTagihan = true,
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListTagihan = navigateBack
                )
            }
            TypeResult.KONFIRMASI_SEKALIGUS_MEMPROSES_LAPORAN_KELUHAN -> {
                ResultKeluhanContent(
                    modifier = Modifier.padding(innePadding),
                    resultLaporanKeluhan = resultLaporanKeluhan?: ResultLaporanKeluhan(),
                    description = stringResource(R.string.description_success_process_complain),
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListKeluhan = navigateBack,
                    title = stringResource(R.string.title_success_process_complain)
                )
            }
            TypeResult.LAPORAN_KELUHAN_SELESAI_DIPROSES -> {
                ResultKeluhanContent(
                    modifier = Modifier.padding(innePadding),
                    resultLaporanKeluhan = resultLaporanKeluhan?: ResultLaporanKeluhan(),
                    title = stringResource(R.string.title_success_finished_complain),
                    description = stringResource(R.string.description_finished_complain),
                    navigateToDashboard = navigateToDashboard,
                    navigateBackToListKeluhan = navigateBack
                )
            }
            TypeResult.BUAT_PENYEWAAN -> {
                ResultCreatePenyewaanContent(
                    modifier = Modifier.padding(innePadding),
                    resultCreatePenyewaan = resultCreatePenyewaan?: ResultCreatePenyewaan(),
                    navigateToDetailPenyewaan = navigateToDetailPenyewaan,
                    navigateBackToListPenyewaan = navigateBack
                )
            }
        }
    }
}

@Composable
fun ResultCreatePenyewaanContent(
    modifier: Modifier = Modifier,
    resultCreatePenyewaan: ResultCreatePenyewaan,
    navigateToDetailPenyewaan: () -> Unit = {},
    navigateBackToListPenyewaan: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.ic_success),
                contentDescription = "Berhasil Buat Penyewaan",
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
        item {
            Text(
                text = stringResource(R.string.title_success_make_a_rental),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
        item {
            InfoResultCreatePenyewaanCard(
                resultCreatePenyewaan = resultCreatePenyewaan
            )
        }
        item { Spacer(Modifier.height(50.dp)) }
        item {
            ActionOutlineButton(
                text = stringResource(R.string.back),
                onClick = navigateBackToListPenyewaan,
                modifier = Modifier.fillMaxWidth(),
                height = 45.dp,
                shape = RoundedCornerShape(12.dp),
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            ActionButton(
                text = stringResource(R.string.look_detail),
                onClick = navigateToDetailPenyewaan,
                modifier = Modifier.fillMaxWidth(),
                height = 45.dp,
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}
@Composable
private fun ResultTagihanContent(
    modifier: Modifier = Modifier,
    resultTagihan: ResultTagihan,
    title: String,
    subtitle: String,
    description: String = "",
    icon: Int = R.drawable.ic_success,
    isTitleRedColor: Boolean = false,
    isCreateTagihan: Boolean = false,
    navigateToDashboard: () -> Unit = {},
    navigateBackToListTagihan: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Image(
                painter = painterResource(icon),
                contentDescription = "Berhasil Pembayaran Tagihan",
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = if (isTitleRedColor) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
        item {
            InfoResulTagihanCard(
                resultTagihan = resultTagihan,
                description = description,
                isCreateTagihan = isCreateTagihan
            )
        }
        item { Spacer(Modifier.height(30.dp)) }
        item {
            ActionOutlineButton(
                text = stringResource(R.string.back),
                onClick = navigateBackToListTagihan,
                modifier = Modifier.fillMaxWidth(),
                height = 45.dp,
                shape = RoundedCornerShape(12.dp),
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            ActionButton(
                text = stringResource(R.string.dashboard),
                onClick = navigateToDashboard,
                modifier = Modifier.fillMaxWidth(),
                height = 45.dp,
                shape = RoundedCornerShape(12.dp)
            )
        }

    }
}

@Composable
private fun ResultKeluhanContent(
    modifier: Modifier = Modifier,
    resultLaporanKeluhan: ResultLaporanKeluhan,
    title: String,
    description: String,
    navigateToDashboard: () -> Unit = {},
    navigateBackToListKeluhan: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Image(
                painter = painterResource(R.drawable.ic_success),
                contentDescription = "Berhasil Buat Laporan Keluhan",
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
        item {
            InfoResultBuatKeluhanCard(
                resultBuatKeluhan = resultLaporanKeluhan,
                description = description
            )
        }
        item { Spacer(Modifier.height(30.dp)) }
        item {
            ActionOutlineButton(
                text = stringResource(R.string.back),
                onClick = navigateBackToListKeluhan,
                modifier = Modifier.fillMaxWidth(),
                height = 45.dp,
                shape = RoundedCornerShape(12.dp),
            )
        }
        item { Spacer(Modifier.height(15.dp)) }
        item {
            ActionButton(
                text = stringResource(R.string.dashboard),
                onClick = navigateToDashboard,
                modifier = Modifier.fillMaxWidth(),
                height = 45.dp,
                shape = RoundedCornerShape(12.dp)
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun ResultScreePreview() {

    val resultLaporan = ResultLaporanKeluhan(
        idLaporan = "iehfiehfiehfohfoewihf",
        statusLaporan = "Menunggu Konfirmasi",
        namaPelapor = "Ndim ndim",
        judulLaporan = "Kamar Boror",
        nomorKamar = 4,
    )
    val resultTagihan =  ResultTagihan(
        idTagihan = "hefheifheifhwohfoiewhfowieejfiwjriofjoriwjf",
        statusTagihan = "Belum Lunas",
        periodStart = "10 Jan 2026",
        periodEnd = "10 Mar 2026",
        jumlahDibayar = 766000,
        nomorKamar = 4,
        alasanPenolakan = "Burik gambar"
    )

    val resultCreatePenyewaan = ResultCreatePenyewaan(
        idPenyewaan = "ieieijfeijfief",
        status = "Belum Lunas",
        nomorKamar = 4,
        penghuni = "Haji dan Huji",
        totalTagihan = 7867777
    )
    KosRvdAppTheme {
        ResultScreen(
            typeResult = TypeResult.BAYAR_TAGIHAN,
            resultLaporanKeluhan = resultLaporan,
            resultTagihan = resultTagihan,
            resultCreatePenyewaan = resultCreatePenyewaan,
            navigateToDashboard = {},
            navigateBack = {}
        )
    }
}

