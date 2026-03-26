package com.kosrvd.app.core.presentation.result.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.DoorFront
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.presentation.navigation.models.ResultCreatePenyewaan
import com.kosrvd.app.presentation.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.presentation.navigation.models.ResultTagihan
import com.kosrvd.app.core.presentation.designsystem.component.text.BackgroundInfoText
import com.kosrvd.app.core.presentation.designsystem.component.text.IconTextInfo
import com.kosrvd.app.core.presentation.designsystem.component.text.StatusBackgroundText
import com.kosrvd.app.core.presentation.designsystem.theme.KosRvdAppTheme
import com.kosrvd.app.core.presentation.utils.dashedBorder
import com.kosrvd.app.core.presentation.utils.toNumber
import com.kosrvd.app.core.presentation.utils.toRupiahFormat

@Composable
fun InfoResulTagihanCard(
    modifier: Modifier = Modifier,
    resultTagihan: ResultTagihan,
    description: String = "",
    isCreateTagihan: Boolean
) {
    val lastTitleSection = if (isCreateTagihan) R.string.amount_bill_with_a_colon else R.string.amount_paid_with_a_colon
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp),
                strokeWidth = 1.dp,
                dashLength = 10.dp,
                gapLength = 10.dp
            )
            .padding(1.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            RowTextInfoResult(
                icon = Icons.AutoMirrored.Filled.Article,
                titleTextInfo = stringResource(R.string.id_bill_with_a_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultTagihan.idTagihan,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.ChangeCircle,
                titleTextInfo = stringResource(R.string.bill_status_with_a_colon),
                contextTextResult = {
                    StatusBackgroundText(
                        status = resultTagihan.statusTagihan,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.DoorFront,
                titleTextInfo = stringResource(R.string.number_room_with_a_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        text = resultTagihan.nomorKamar.toNumber(),
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.EventAvailable,
                titleTextInfo = stringResource(R.string.period_with_a_color),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 190.dp),
                        text = "${resultTagihan.periodStart} - ${resultTagihan.periodEnd}",
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = ImageVector.vectorResource(R.drawable.ic_jumlah_bayar),
                titleTextInfo = stringResource(lastTitleSection),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultTagihan.jumlahDibayar.toRupiahFormat(),
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
            if (resultTagihan.alasanPenolakan.isNotBlank()){
                Spacer(Modifier.height(15.dp))
                RowTextInfoResult(
                    icon = Icons.AutoMirrored.Filled.Comment,
                    titleTextInfo = stringResource(R.string.reason_with_colon),
                    contextTextResult = {
                        BackgroundInfoText(
                            modifier = Modifier.widthIn(max = 175.dp),
                            text = resultTagihan.alasanPenolakan,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                )
            }
            if (description.isNotBlank()){
                Spacer(Modifier.height(15.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }

        }

    }
}

@Composable
fun InfoResultBuatKeluhanCard(
    modifier: Modifier = Modifier,
    resultBuatKeluhan: ResultLaporanKeluhan,
    description: String,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp),
                strokeWidth = 1.dp,
                dashLength = 10.dp,
                gapLength = 10.dp
            )
            .padding(1.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            RowTextInfoResult(
                icon = Icons.AutoMirrored.Filled.Article,
                titleTextInfo = stringResource(R.string.id_complain_with_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultBuatKeluhan.idLaporan,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.ChangeCircle,
                titleTextInfo = stringResource(R.string.status_with_colon),
                contextTextResult = {
                    StatusBackgroundText(
                        status = resultBuatKeluhan.statusLaporan,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.DoorFront,
                titleTextInfo = stringResource(R.string.number_room_with_a_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        text = resultBuatKeluhan.nomorKamar.toNumber(),
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.AccountBox,
                titleTextInfo = stringResource(R.string.reporter_with_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultBuatKeluhan.namaPelapor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.Title,
                titleTextInfo = stringResource(R.string.title_with_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 200.dp),
                        text = resultBuatKeluhan.judulLaporan,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }

    }
}

@Composable
fun InfoResultCreatePenyewaanCard(
    modifier: Modifier = Modifier,
    resultCreatePenyewaan: ResultCreatePenyewaan
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp),
                strokeWidth = 1.dp,
                dashLength = 10.dp,
                gapLength = 10.dp
            )
            .padding(1.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            RowTextInfoResult(
                icon = Icons.AutoMirrored.Filled.Article,
                titleTextInfo = stringResource(R.string.id_rental_with_a_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultCreatePenyewaan.idPenyewaan,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.ChangeCircle,
                titleTextInfo = stringResource(R.string.status_with_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        text = resultCreatePenyewaan.status,
                        colorBg = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.DoorFront,
                titleTextInfo = stringResource(R.string.number_room_with_a_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        text = resultCreatePenyewaan.nomorKamar.toNumber(),
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = Icons.Default.Group,
                titleTextInfo = stringResource(R.string.resident_with_a_colon),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultCreatePenyewaan.penghuni,
                    )
                },
            )
            Spacer(Modifier.height(15.dp))
            RowTextInfoResult(
                icon = ImageVector.vectorResource(R.drawable.ic_jumlah_bayar),
                titleTextInfo = stringResource(R.string.total_bill_with_a_colon ),
                contextTextResult = {
                    BackgroundInfoText(
                        modifier = Modifier.widthIn(max = 170.dp),
                        text = resultCreatePenyewaan.totalTagihan.toRupiahFormat(),
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        }
    }
}

@Composable
private fun RowTextInfoResult(
    icon: ImageVector,
    titleTextInfo: String,
    contextTextResult: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        IconTextInfo(
            icon = icon,
            text = titleTextInfo,
            colorText = MaterialTheme.colorScheme.primary,
            colorIcon = MaterialTheme.colorScheme.primary,
            spacing = 5.dp,
            sizeIcon = 32.dp
        )
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            contextTextResult()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoResulTagihanCardPreview() {
    KosRvdAppTheme {
        InfoResulTagihanCard(
            modifier = Modifier.padding(16.dp),
            resultTagihan = ResultTagihan(
                idTagihan = "hefheifheifhwohfoiewhfowieejfiwjriofjoriwjf",
                statusTagihan = "Belum Lunas",
                periodStart = "16 Feb 2026",
                periodEnd = "15 Mar 2026",
                jumlahDibayar = 766000,
                nomorKamar = 4,
            ),
            isCreateTagihan = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoResultBuatKeluhanCardPreview() {
    KosRvdAppTheme {
        InfoResultBuatKeluhanCard(
            modifier = Modifier.padding(16.dp),
            resultBuatKeluhan = ResultLaporanKeluhan(
                idLaporan = "iehfiehfiehfohfoewihf",
                statusLaporan = "Menunggu Konfirmasi",
                namaPelapor = "Ndim ndim",
                judulLaporan = "Kamar Boror",
                nomorKamar = 4,
            ),
            description = stringResource(R.string.description_success_process_complain)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoResultCreatePenyewaanCardPreview() {
    KosRvdAppTheme {
        InfoResultCreatePenyewaanCard(
            modifier = Modifier.padding(16.dp),
            resultCreatePenyewaan = ResultCreatePenyewaan(
                idPenyewaan = "iehfiehfiehfohfoewihf",
                status = "Aktif",
                penghuni = "Ndim ndim",
                totalTagihan = 765000,
                nomorKamar = 4,
            )
        )
    }
}
