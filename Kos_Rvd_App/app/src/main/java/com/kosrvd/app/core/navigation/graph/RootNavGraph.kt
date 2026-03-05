package com.kosrvd.app.core.navigation.graph

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kosrvd.app.MainViewModel
import com.kosrvd.app.core.data.constant.Constant
import com.kosrvd.app.core.domain.utils.rememberCustomToastHostState
import com.kosrvd.app.core.navigation.NavigationGraph
import com.kosrvd.app.core.navigation.NavigationScreen
import com.kosrvd.app.core.navigation.models.CustomNavTypes
import com.kosrvd.app.core.navigation.models.ResultCreatePenyewaan
import com.kosrvd.app.core.navigation.models.ResultLaporanKeluhan
import com.kosrvd.app.core.navigation.models.ResultTagihan
import com.kosrvd.app.core.navigation.models.TemporaryData
import com.kosrvd.app.core.presentation.designsystem.animation.ScaleTransitionDirection
import com.kosrvd.app.core.presentation.designsystem.animation.scaleIntoContainer
import com.kosrvd.app.core.presentation.designsystem.animation.scaleOutOfContainer
import com.kosrvd.app.core.presentation.utils.CustomToastResultConfig
import com.kosrvd.app.core.presentation.utils.ObserveAsEvents
import com.kosrvd.app.core.presentation.utils.ObserveCustomToastResults
import com.kosrvd.app.core.presentation.utils.ObserveSnackbarResults
import com.kosrvd.app.core.presentation.utils.ShakeTargetBuatLaporanKeluhan
import com.kosrvd.app.core.presentation.utils.SnackbarResultConfig
import com.kosrvd.app.core.presentation.utils.navigateBackWithSendKey
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeEdit
import com.kosrvd.app.feature.management.presentation.designsystem.utils.TypeNotification
import com.kosrvd.app.feature.management.presentation.ui.screen.MainScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.buat_laporan_keluhan.BuatLaporanKeluhanEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.buat_laporan_keluhan.BuatLaporanKeluhanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.buat_laporan_keluhan.BuatLaporanKeluhanViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.detail_keluhan.DetailKeluhanEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.detail_keluhan.DetailKeluhanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.keluhan.detail_keluhan.DetailKeluhanViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi.NotificationEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi.NotificationScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.notifikasi.NotificationViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.preview_image.PreviewImageScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_profile.DetailProfileEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_profile.DetailProfileScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_profile.DetailProfileViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_sewa_kamar.DetailSewaKamarEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_sewa_kamar.DetailSewaKamarScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.detail_sewa_kamar.DetailSewaKamarViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_data_profile.EditDataProfileEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_data_profile.EditDataProfileScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_data_profile.EditDataProfileViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_photo_profile.EditPhotoProfileEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_photo_profile.EditPhotoProfileScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.edit_photo_profile.EditPhotoProfileViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.reset_password.ResetPasswordEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.reset_password.ResetPasswordScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.profile.reset_password.ResetPasswordViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.result.ResultScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan.BuatTagihanEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan.BuatTagihanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.buat_tagihan.BuatTagihanViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.detail_tagihan.DetailTagihanEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.detail_tagihan.DetailTagihanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.detail_tagihan.DetailTagihanViewModel
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.pengaturan_tagihan.PengaturanTagihanEvents
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.pengaturan_tagihan.PengaturanTagihanScreen
import com.kosrvd.app.feature.management.presentation.ui.screen.tagihan.pengaturan_tagihan.PengaturanTagihanViewModel
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

@Composable
fun RootNavGraph(
    navController: NavHostController,
    isAuthenticationRequired: Boolean,
    mainViewModel: MainViewModel
) {
    val startDestination = if (isAuthenticationRequired) {
        NavigationGraph.MainGraph
    } else {
        NavigationGraph.AuthGraph
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            scaleIntoContainer()
        },
        exitTransition = {
            scaleOutOfContainer(ScaleTransitionDirection.INWARDS)
        },
        popEnterTransition = {
            scaleIntoContainer(ScaleTransitionDirection.OUTWARDS)
        },
        popExitTransition = {
            scaleOutOfContainer()
        }
    ) {
        authGraph(navController = navController)
        composable<NavigationGraph.MainGraph> {
            val snackBarHostState = remember { SnackbarHostState() }

            ObserveSnackbarResults(
                navBackStackEntry = it,
                snackbarHostState = snackBarHostState,
                SnackbarResultConfig(
                    key = Constant.DELETE_TAGIHAN_KEY,
                    message = "DATA TAGIHAN BERHASIL DIHAPUS"
                ),
                SnackbarResultConfig(
                    key = Constant.DELETE_KELUHAN_KEY,
                    message = "DATA KELUHAN BERHASIL DIHAPUS"
                )
            )

            MainScreen(
                mainViewModel = mainViewModel,
                rootNavController = navController,
                snackbarHostState = snackBarHostState
            )
        }

        penghuniGraph(navController = navController)
        pengumumanGraph(navController = navController)
        penyewaanGraph(navController = navController)
        akunPenggunaGraph(navController = navController)
        parkirHarianMobilGraph(navController = navController)
        zonaParkirGraph(navController = navController)
        kamarGraph(navController = navController)

        composable<NavigationScreen.DetailTagihan> { backStackEntry ->
            val idTagihan = backStackEntry.toRoute<NavigationScreen.DetailTagihan>().idTagihan

            val detailTagihanViewModel = hiltViewModel<DetailTagihanViewModel>()
            val detailTagihanUiState by detailTagihanViewModel.state.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()
            val uploadProofOfPaymentRequester = remember { BringIntoViewRequester() }

            ObserveAsEvents(detailTagihanViewModel.events) { event ->
                when (event) {
                    DetailTagihanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }

                    DetailTagihanEvents.NavigateBackToSendDeleteSnackBar -> {
                        navController.navigateBackWithSendKey(Constant.DELETE_TAGIHAN_KEY)
                    }

                    is DetailTagihanEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(event.imageUri.toString())
                        navController.navigate(NavigationScreen.PreviewImageScreen(safeUrl))
                    }

                    is DetailTagihanEvents.NavigateToResult -> {
                        navController.navigate(
                            NavigationScreen.ResultScreen(
                                typeResult = event.typeResult,
                                resultTagihan = event.resultTagihan,
                                resultLaporanKeluhan = null,
                                resultCreatePenyewaan = null
                            )
                        ) {
                            popUpTo(NavigationScreen.DetailTagihan(idTagihan)) {
                                inclusive = true
                            }
                        }
                    }

                    is DetailTagihanEvents.ShowSnackBarError -> {
                        scope.launch {
                            if (event.message == "Bukti pembayaran belum ada"){
                                uploadProofOfPaymentRequester.bringIntoView()
                            }
                            customToastHostState.showToast(event.message)
                        }
                    }
                }
            }

            LaunchedEffect(idTagihan) {
                detailTagihanViewModel.loadDetailTagihan(idTagihan)
            }

            DetailTagihanScreen(
                idTagihan = idTagihan,
                customToastHostState = customToastHostState,
                detailTagihanUiState = detailTagihanUiState,
                detailTagihanActions = detailTagihanViewModel::onActions,
                uploadProofOfPaymentRequester = uploadProofOfPaymentRequester
            )
        }

        composable<NavigationScreen.BuatTagihanScreen> {
            val buatTagihanViewModel = hiltViewModel<BuatTagihanViewModel>()
            val buatTagihanUiState by buatTagihanViewModel.state.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(buatTagihanViewModel.events) { event ->
                when (event) {
                    BuatTagihanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }

                    is BuatTagihanEvents.NavigateToResult -> {
                        navController.navigate(
                            NavigationScreen.ResultScreen(
                                typeResult = event.typeResult,
                                resultTagihan = event.resultBuatTagihan
                            )
                        ) {
                            popUpTo(NavigationScreen.BuatTagihanScreen) {
                                inclusive = true
                            }
                        }
                    }

                    is BuatTagihanEvents.ShowErrorBanner -> {
                        scope.launch {
                            customToastHostState.showToast(event.message, durationMillis = 5000L)
                        }
                    }
                }
            }

            BuatTagihanScreen(
                buatTagihanUiState = buatTagihanUiState,
                buatTagihanActions = buatTagihanViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }

        composable<NavigationScreen.PengaturanTagihanScreen> {
            val pengaturanTagihanViewModel = hiltViewModel<PengaturanTagihanViewModel>()
            val pengaturanTagihanUiState by pengaturanTagihanViewModel.state.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()
            var colorShowBanner by remember { mutableStateOf(Color.Unspecified) }

            ObserveAsEvents(pengaturanTagihanViewModel.events) { events ->
                when(events){
                    PengaturanTagihanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is PengaturanTagihanEvents.ShowSnackBar -> {
                        scope.launch {
                            colorShowBanner = if (events.isRedColor) Color(0xFFBA1A1A) else Color(0xFF006877)
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            PengaturanTagihanScreen(
                pengaturanTagihanUiState = pengaturanTagihanUiState,
                pengaturanTagihanActions = pengaturanTagihanViewModel::onActions,
                customToastHostState = customToastHostState,
                colorToast = colorShowBanner
            )
        }

        composable<NavigationScreen.NotificationScreen> {
            val notificationViewModel = hiltViewModel<NotificationViewModel>()
            val notificationUiState by notificationViewModel.state.collectAsStateWithLifecycle()

            ObserveAsEvents(notificationViewModel.events) { events ->
                when (events) {
                    is NotificationEvents.NavigateBack -> {
                        navController.navigateUp()
                    }

                    is NotificationEvents.NavigateToDetailNotification -> {
                        when (events.typeNotification) {
                            TypeNotification.TAGIHAN -> {
                                navController.navigate(NavigationScreen.DetailTagihan(events.idDetailReferensi))
                            }

                            TypeNotification.PENGUMUMAN -> {
                                navController.navigate(NavigationGraph.PengumumanGraph)
                            }

                            TypeNotification.LAPORAN_KELUHAN -> {
                                navController.navigate(NavigationScreen.DetailKeluhanScreen(events.idDetailReferensi))
                            }
                        }
                    }
                }
            }

            NotificationScreen(
                notificationUiState = notificationUiState,
                notificationActions = notificationViewModel::onActions
            )
        }

        composable<NavigationScreen.PreviewImageScreen>(
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                    )
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                    )
                )
            },
            popEnterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                    )
                )
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                    )
                )
            },
        ) { navBackStackEntry ->
            val url = navBackStackEntry.toRoute<NavigationScreen.PreviewImageScreen>().imageUrl
            PreviewImageScreen(
                imageUrl = url,
                onNavigateBack = { navController.navigateUp() }
            )
        }


        // In your navigation graph setup
        composable<NavigationScreen.ResultScreen>(
            typeMap = mapOf(
                typeOf<ResultTagihan?>() to CustomNavTypes.ResultTagihanType,
                typeOf<ResultLaporanKeluhan?>() to CustomNavTypes.ResultLaporanKeluhanType,
                typeOf<ResultCreatePenyewaan?>() to CustomNavTypes.ResultCreatePenyewaanType
            )
        ) { backStackEntry ->
            val result = backStackEntry.toRoute<NavigationScreen.ResultScreen>()

            val navigateToDetailPenyewaan = {
                navController.navigate(
                    NavigationScreen.DetailPenyewaanScreen(
                        result.resultCreatePenyewaan?.idPenyewaan ?: ""
                    )
                ) {
                    popUpTo(NavigationScreen.ResultScreen) {
                        inclusive = true
                    }
                }
            }
            ResultScreen(
                typeResult = result.typeResult,
                resultTagihan = result.resultTagihan,
                resultLaporanKeluhan = result.resultLaporanKeluhan,
                resultCreatePenyewaan = result.resultCreatePenyewaan,
                navigateToDetailPenyewaan = navigateToDetailPenyewaan,
                navigateToDashboard = {
                    navController.navigate(NavigationGraph.MainGraph) {
                        popUpTo(NavigationGraph.MainGraph) {
                            inclusive = true
                        }
                    }
                },
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }

        composable<NavigationScreen.DetailProfileScreen> {
            val detailProfileViewModel = hiltViewModel<DetailProfileViewModel>()
            val detailProfileUiState by detailProfileViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(detailProfileViewModel.events) { events ->
                when(events){
                    DetailProfileEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is DetailProfileEvents.NavigateToDetailSewa -> {
                        navController.navigate(NavigationScreen.DetailKamarSewaScreen(events.idPenyewa))
                    }
                    is DetailProfileEvents.NavigateToEditDataProfile -> {
                        navController.navigate(NavigationScreen.EditDataProfileScreen(
                            typeEdit = events.typeEdit,
                            temporaryData = events.temporaryData,
                            idAkun = events.idAkun
                        ))
                    }
                    is DetailProfileEvents.NavigateToEditPhotoProfile -> {
                        navController.navigate(NavigationScreen.EditPhotoProfileScreen(
                            photoUrl = events.photoUrl,
                            idAkun = events.idAkun
                        ))
                    }
                    is DetailProfileEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(events.imageUrl)
                        navController.navigate(NavigationScreen.PreviewImageScreen(safeUrl))
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = it,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.UPDATE_NAME_KEY,
                    message = "NAMA BERHASIL DIPERBARUI"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_EMAIL_KEY,
                    message = "VERIFIKASI EMAIL BERHASIL DIKIRIM"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_PHOTO_KEY,
                    message = "FOTO BERHASIL DIPERBARUI"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_ALAMAT_KEY,
                    message = "ALAMAT BERHASIL DIPERBARUI"
                ),
                CustomToastResultConfig(
                    key = Constant.UPDATE_NO_HP_KEY,
                    message = "NO HP BERHASIL DIPERBARUI"
                ),
                durationMillis = 5000L
            )


            DetailProfileScreen(
                detailProfileUiState = detailProfileUiState,
                detailProfileActions = detailProfileViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }

        composable<NavigationScreen.ResetPasswordScreen> { navBackStackEntry ->
            val resetPasswordViewModel = hiltViewModel<ResetPasswordViewModel>()
            val resetPasswordUiState by resetPasswordViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()

            ObserveAsEvents(resetPasswordViewModel.events) { events ->
                when(events){
                    ResetPasswordEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is ResetPasswordEvents.NavigateToEditDataProfileChangePassword -> {
                        navController.navigate(NavigationScreen.EditDataProfileScreen(
                            typeEdit = TypeEdit.GANTI_PASSWORD,
                            idAkun = events.idAkun
                        ))
                    }
                    is ResetPasswordEvents.ShowSnackBar -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            ObserveCustomToastResults(
                navBackStackEntry = navBackStackEntry,
                customToasHostState = customToastHostState,
                CustomToastResultConfig(
                    key = Constant.UPDATE_PASSWORD_KEY,
                    message = "PASSWORD BERHASIL DIPERBARUI"
                )
            )

            ResetPasswordScreen(
                resetPasswordUiState = resetPasswordUiState,
                resetPasswordActions = resetPasswordViewModel::onActions,
                customToastHostState = customToastHostState
            )

        }

        composable<NavigationScreen.EditDataProfileScreen>(
            typeMap = mapOf(typeOf<TemporaryData?>() to CustomNavTypes.TemporaryDataType)
        ) { backStackEntry ->
            val argument = backStackEntry.toRoute<NavigationScreen.EditDataProfileScreen>()
            val editDataProfileViewModel = hiltViewModel<EditDataProfileViewModel>()
            val editDataProfileUiState by editDataProfileViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()
            ObserveAsEvents(editDataProfileViewModel.events) { events ->
                when(events){
                    EditDataProfileEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    is EditDataProfileEvents.NavigateBackSuccessEditDataProfile -> {
                        when(events.typeEdit){
                            TypeEdit.NAMA -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_NAME_KEY)
                            }
                            TypeEdit.EMAIL -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_EMAIL_KEY)
                            }
                            TypeEdit.GANTI_PASSWORD -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_PASSWORD_KEY)
                            }
                            TypeEdit.NO_HP -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_NO_HP_KEY)
                            }
                            TypeEdit.ALAMAT -> {
                                navController.navigateBackWithSendKey(Constant.UPDATE_ALAMAT_KEY)
                            }
                        }
                    }
                    is EditDataProfileEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            EditDataProfileScreen(
                editDataProfileUiState = editDataProfileUiState,
                editDataProfileActions = editDataProfileViewModel::onActions,
                customToastHostState = customToastHostState,
                typeEdit = argument.typeEdit,
                temporaryData = argument.temporaryData
            )
        }


        composable<NavigationScreen.EditPhotoProfileScreen> { navBackStackEntry ->
            val arguments = navBackStackEntry.toRoute<NavigationScreen.EditPhotoProfileScreen>()
            val editPhotoProfileViewModel = hiltViewModel<EditPhotoProfileViewModel>()
            val editPhotoProfileUiState by editPhotoProfileViewModel.state.collectAsStateWithLifecycle()
            val customToastHostState = rememberCustomToastHostState()
            val scope = rememberCoroutineScope()
            ObserveAsEvents(editPhotoProfileViewModel.events) { events ->
                when(events){
                    EditPhotoProfileEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                    EditPhotoProfileEvents.NavigateBackToSendUpdatePhoto -> {
                        navController.navigateBackWithSendKey(Constant.UPDATE_PHOTO_KEY)
                    }
                    is EditPhotoProfileEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            LaunchedEffect(arguments) {
                editPhotoProfileViewModel.initialPhotoAndIdAkun(
                    oldPhotoUri = arguments.photoUrl,
                    idAkun = arguments.idAkun
                )
            }

            EditPhotoProfileScreen(
                editPhotoProfileUiState = editPhotoProfileUiState,
                editPhotoProfileActions = editPhotoProfileViewModel::onActions,
                customToastHostState = customToastHostState
            )

        }
        composable<NavigationScreen.DetailKamarSewaScreen> { navBackStackEntry ->
            val idPenyewa = navBackStackEntry.toRoute<NavigationScreen.DetailKamarSewaScreen>().idPenyewa
            val detailSewaKamarViewModel = hiltViewModel<DetailSewaKamarViewModel>()
            val detailSewaKamarUiState by detailSewaKamarViewModel.state.collectAsStateWithLifecycle()

            ObserveAsEvents(detailSewaKamarViewModel.events) { events ->
                when(events){
                    DetailSewaKamarEvents.NavigateBack -> {
                        navController.navigateUp()
                    }
                }
            }

            LaunchedEffect(idPenyewa) {
                detailSewaKamarViewModel.loadDataPenyewaan(idPenyewa)
            }

            DetailSewaKamarScreen(
                idPenyewa = idPenyewa,
                detailSewaKamarUiState = detailSewaKamarUiState,
                detailsSewaKamarActions = detailSewaKamarViewModel::onActions
            )
        }


        composable<NavigationScreen.DetailKeluhanScreen> { navBackStackEntry ->
            val idKeluhan =
                navBackStackEntry.toRoute<NavigationScreen.DetailKeluhanScreen>().idKeluhan
            val detailKeluhanViewModel = hiltViewModel<DetailKeluhanViewModel>()
            val detailKeluhanUiState by detailKeluhanViewModel.state.collectAsStateWithLifecycle()

            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(detailKeluhanViewModel.events) { events ->
                when (events) {
                    DetailKeluhanEvents.NavigateBack -> {
                        navController.navigateUp()
                    }

                    DetailKeluhanEvents.NavigateBackToSendDeleteSnackBar -> {
                        navController.navigateBackWithSendKey(Constant.DELETE_KELUHAN_KEY)
                    }

                    is DetailKeluhanEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(events.imageUri.toString())
                        navController.navigate(NavigationScreen.PreviewImageScreen(safeUrl))
                    }

                    is DetailKeluhanEvents.NavigateToResult -> {
                        navController.navigate(
                            NavigationScreen.ResultScreen(
                                typeResult = events.result,
                                resultLaporanKeluhan = events.keluhanResult
                            )
                        ) {
                            popUpTo(NavigationScreen.DetailKeluhanScreen(idKeluhan)) {
                                inclusive = true
                            }
                        }
                    }

                    is DetailKeluhanEvents.ShowBannerError -> {
                        scope.launch {
                            customToastHostState.showToast(events.message)
                        }
                    }
                }
            }

            LaunchedEffect(idKeluhan) {
                detailKeluhanViewModel.loadDetailKeluhan(idKeluhan)
            }

            DetailKeluhanScreen(
                idKeluhan = idKeluhan,
                detailKeluhanUiState = detailKeluhanUiState,
                detailKeluhanActions = detailKeluhanViewModel::onActions,
                customToastHostState = customToastHostState
            )
        }


        composable<NavigationScreen.BuatKeluhanScreen> {
            val buatLaporanKeluhanViewModel = hiltViewModel<BuatLaporanKeluhanViewModel>()
            val buatLaporanKeluhanUiState by buatLaporanKeluhanViewModel.state.collectAsStateWithLifecycle()

            // Trigger Error Text Field
            var titleShakeTrigger by remember { mutableIntStateOf(0) }
            var descriptionShakeTrigger by remember { mutableIntStateOf(0) }

            val scope = rememberCoroutineScope()
            val customToastHostState = rememberCustomToastHostState()

            ObserveAsEvents(buatLaporanKeluhanViewModel.events) { event ->
                when (event) {
                    is BuatLaporanKeluhanEvents.NavigateUp -> {
                        navController.navigateUp()
                    }

                    is BuatLaporanKeluhanEvents.NavigateToPreviewImage -> {
                        val safeUrl = Uri.encode(event.imageUri.toString())
                        navController.navigate(NavigationScreen.PreviewImageScreen(safeUrl))
                    }

                    is BuatLaporanKeluhanEvents.NavigateToResult -> {
                        navController.navigate(
                            NavigationScreen.ResultScreen(
                                typeResult = event.typeResult,
                                resultTagihan = null,
                                resultLaporanKeluhan = event.resultLaporanKeluhan,
                            )
                        ) {
                            popUpTo(NavigationScreen.BuatKeluhanScreen) {
                                inclusive = true
                            }
                        }
                    }

                    is BuatLaporanKeluhanEvents.ShowToast -> {
                        Toast.makeText(navController.context, event.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is BuatLaporanKeluhanEvents.ShowSnackBarError -> {
                        scope.launch {
                            customToastHostState.showToast(event.message)
                        }
                    }

                    is BuatLaporanKeluhanEvents.ShakeTextField -> {
                        when (event.target) {
                            ShakeTargetBuatLaporanKeluhan.BOTH -> {
                                titleShakeTrigger++
                                descriptionShakeTrigger++
                            }

                            ShakeTargetBuatLaporanKeluhan.TITLE -> {
                                titleShakeTrigger++
                            }

                            ShakeTargetBuatLaporanKeluhan.DESCRIPTION -> {
                                descriptionShakeTrigger++
                            }
                        }
                    }
                }
            }

            BuatLaporanKeluhanScreen(
                buatLaporanKeluhanUiState = buatLaporanKeluhanUiState,
                buatLaporanKeluhanActions = buatLaporanKeluhanViewModel::onActions,
                titleShakeTrigger = titleShakeTrigger,
                descriptionShakeTrigger = descriptionShakeTrigger,
                customToastHostState = customToastHostState
            )
        }
    }
}