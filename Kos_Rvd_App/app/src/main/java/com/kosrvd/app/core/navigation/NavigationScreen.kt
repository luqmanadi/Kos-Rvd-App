package com.kosrvd.app.core.navigation

import com.kosrvd.app.core.navigation.models.EditPindahKamarType
import com.kosrvd.app.core.navigation.models.EditTypeKamar
import com.kosrvd.app.core.navigation.models.ResultCreatePenyewaan
import com.kosrvd.app.core.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.core.navigation.models.TambahPenghuniType
import com.kosrvd.app.core.navigation.models.TemporaryData
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEdit
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditKamar
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEditZonaParkir
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeResult
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationScreen {

    val route : String

    // Auth Graph
    @Serializable
    data object OnBoardingScreen : NavigationScreen {
        override val route: String = "onboarding"
    }
    @Serializable
    data object LoginScreen : NavigationScreen {
        override val route: String = "login"
    }
    @Serializable
    data object ForgotPasswordScreen : NavigationScreen {
        override val route: String = "forgot_password"
    }


    // Main Graph Semuanya ada di sini
    @Serializable
    data object DashboardScreen : NavigationScreen {
        override val route: String = "dashboard"
    }

    @Serializable
    data object NotificationScreen : NavigationScreen {
        override val route: String = "notification"
    }

    @Serializable
    data object ListPenghuniScreen : NavigationScreen {
        override val route: String = "list_penghuni"
    }

    @Serializable
    data class DetailPenghuniScreen(val penghuniId: String) : NavigationScreen {
        override val route: String = "detail_penghuni"
    }

    @Serializable
    data class PreviewImageScreen(val imageUrl: String) : NavigationScreen {
        override val route: String = "preview_image"
    }

    @Serializable
    data class ResultScreen(
        val typeResult: TypeResult,
        val resultTagihan: ResultTagihan? = null,
        val resultLaporanKeluhan: ResultLaporanKeluhan? = null,
        val resultCreatePenyewaan: ResultCreatePenyewaan? = null
    ): NavigationScreen {
        override val route: String = "result"
    }


    // Pengumuman Graph di dalam Main Graph
    @Serializable
    data object PengumumanScreen : NavigationScreen {
        override val route: String = "pengumuman"
    }
    @Serializable
    data object BuatPengumumanScreen : NavigationScreen {
        override val route: String = "buat_pengumuman"
    }


    // Tagihan Graph di dalam Main Graph
    @Serializable
    data object ListTagihanScreen : NavigationScreen {
        override val route: String = "list_tagihan"
    }
    @Serializable
    data class DetailTagihan(val idTagihan: String) : NavigationScreen {
        override val route: String = "detail_tagihan"
    }
    @Serializable
    data object BuatTagihanScreen : NavigationScreen {
        override val route: String = "buat_tagihan"
    }

    @Serializable
    data object PengaturanTagihanScreen: NavigationScreen {
        override val route: String = "pengaturan_tagihan"
    }


    // Keluhan Graph di dalam Main Graph
    @Serializable
    data object ListKeluhanScreen : NavigationScreen {
        override val route: String = "list_keluhan"
    }
    @Serializable
    data class DetailKeluhanScreen(val idKeluhan: String) : NavigationScreen {
        override val route: String = "detail_keluhan"
    }
    @Serializable
    data object BuatKeluhanScreen : NavigationScreen {
        override val route: String = "buat_keluhan"
    }


    // Profile Graph di dalam Main Graph
    @Serializable
    data object ProfileScreen : NavigationScreen {
        override val route: String = "profile"
    }
    @Serializable
    data object ResetPasswordScreen : NavigationScreen {
        override val route: String = "reset_password"
    }
    @Serializable
    data object DetailProfileScreen : NavigationScreen {
        override val route: String = "detail_profile"
    }
    @Serializable
    data class EditDataProfileScreen(val typeEdit: TypeEdit, val temporaryData: TemporaryData? = null, val idAkun: String) :
        NavigationScreen {
        override val route: String = "edit_profile"
    }
    @Serializable
    data class EditPhotoProfileScreen(val photoUrl: String, val idAkun: String) : NavigationScreen {
        override val route: String = "edit_photo_profile"
    }
    @Serializable
    data class DetailKamarSewaScreen(val idPenyewa: String) : NavigationScreen {
        override val route: String = "detail_kamar_sewa"
    }


    // Penyewaan Graph di dalam Main Graph
    @Serializable
    data object ListPenyewaanScreen : NavigationScreen {
        override val route: String = "list_penyewaan"
    }
    @Serializable
    data class DetailPenyewaanScreen(val idPenyewa: String) : NavigationScreen {
        override val route: String = "detail_penyewaan"
    }
    @Serializable
    data object BuatPenyewaanScreen : NavigationScreen {
        override val route: String = "buat_penyewaan"
    }
    @Serializable
    data class TambahPenguniScreen(
        val tambahPenghuniType: TambahPenghuniType
    ) : NavigationScreen {
        override val route: String = "tambah_penghuni"
    }
    @Serializable
    data class PindahKamarScreen(
        val editPindahKamarType: EditPindahKamarType
    ) : NavigationScreen {
        override val route: String = "pindah_kamar"
    }
    @Serializable
    data class EditPemakaianAlatEleketronikScreen(
        val idPenyewaan: String, val idPakaiAlat: String? = null
    ) : NavigationScreen {
        override val route: String = "edit_atau_tambah_pemakaian_alat_elektronik"
    }

    @Serializable
    data class EditPemakaianParkirMobilBulananScreen(
        val idPenyewaan: String, val idPakaiParkiranMobil: String? = null
    ) : NavigationScreen {
        override val route: String = "edit_atau_tambah_pemakaian_parkir_mobil"
    }


    // Akun Pengguna Graph di dalam Main Graph
    @Serializable
    data object ListAkunPenggunaScreen : NavigationScreen {
        override val route: String = "list_akun_pengguna"
    }
    @Serializable
    data class DetailAkunPenggunaScreen(val idAkun: String) : NavigationScreen {
        override val route: String = "detail_akun_pengguna"
    }
    @Serializable
    data object BuatAkunPenggunaScreen : NavigationScreen {
        override val route: String = "buat_akun_pengguna"
    }


    // Riwayat Pemakaian Parkir Mobil Graph di dalam Main Graph
    @Serializable
    data object ListParkirHarianMobilScreen : NavigationScreen {
        override val route: String = "list_parkir_harian_mobil"
    }
    @Serializable
    data class DetailParkirHarianMobilScreen(val idParkirHarianMobil: String) : NavigationScreen {
        override val route: String = "detail_parkir_harian_mobil"
    }
    @Serializable
    data object TambahParkirHarianMobilScreen : NavigationScreen {
        override val route: String = "tambah_parkir_harian_mobil"
    }


    // Zona Parkir Graph di dalam Main Graph
    @Serializable
    data object ListZonaParkirScreen : NavigationScreen {
        override val route: String = "list_zona_parkir"
    }
    @Serializable
    data class DetailZonaParkirScreen(val idZonaParkir: String) : NavigationScreen {
        override val route: String = "detail_zona_parkir"
    }
    @Serializable
    data object TambahZonaParkirScreen : NavigationScreen {
        override val route: String = "tambah_zona_parkir"
    }
    @Serializable
    data class EditZonaParkirScreen(
        val idZonaParkir: String,
        val typeEditZonaParkir: TypeEditZonaParkir,
        val biayaBulanan: Long? = null,
        val biayaHarian: Long? = null,
        val namaZona: String? = null
    ) : NavigationScreen {
        override val route: String = "edit_zona_parkir"
    }


    // Kamar Graph di dalam Main Graph
    @Serializable
    data object ListKamarScreen : NavigationScreen {
        override val route: String = "list_kamar"
    }
    @Serializable
    data class DetailKamarScreen(val idDetailKamar: String) : NavigationScreen {
        override val route: String = "detail_kamar"
    }
    @Serializable
    data object BuatKamarScreen : NavigationScreen {
        override val route: String = "tambah_kamar"
    }
    @Serializable
    data class EditKamarScreen(
        val idKamar: String,
        val typeEditKamar: TypeEditKamar,
        val editTypeKamar: EditTypeKamar
    ) : NavigationScreen {
        override val route: String = "edit_kamar"
    }

}

