package com.kosrvd.app.feature.account.presentation.buat_akun

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.radio_button.CustomRadioButtonV1
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.component.textfield.PasswordTextField
import com.kosrvd.app.core.presentation.designsystem.organism.card.BuktiFotoCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun BuatAkunScreen(
    buatAkunUiState: BuatAkunUiState,
    buatAkunActions: (BuatAkunActions) -> Unit,
    customToastHostState: CustomToastHostState
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = stringResource(R.string.create_account),
                onBackClick = {
                    keyboardController?.hide()
                    buatAkunActions(BuatAkunActions.NavigateBack)
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            ContentBuatAkun(
                buatAkunUiState = buatAkunUiState,
                buatAkunActions = buatAkunActions,
                keyboardController = keyboardController
            )
            CustomToastHost(
                hostState = customToastHostState,
                color = MaterialTheme.colorScheme.error,
                enter = slideInVertically(
                    // Enters by sliding in from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                ),
                exit = slideOutVertically(
                    // Exits by sliding out from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
                )
            )
        }
    }
}

@Composable
fun ContentBuatAkun(
    buatAkunUiState: BuatAkunUiState,
    buatAkunActions: (BuatAkunActions) -> Unit,
    keyboardController: SoftwareKeyboardController?
) {
    val nameState = rememberTextFieldState(buatAkunUiState.name)
    LaunchedEffect(nameState) {
        snapshotFlow { nameState.text.toString() }.collectLatest {
            buatAkunActions(BuatAkunActions.UpdateFillName(it))
        }
    }

    val emailState = rememberTextFieldState(buatAkunUiState.email)
    LaunchedEffect(emailState) {
        snapshotFlow { emailState.text.toString() }.collectLatest {
            buatAkunActions(BuatAkunActions.UpdateFillEmail(it))
        }
    }

    val phoneNumberState = rememberTextFieldState(buatAkunUiState.phoneNumber)
    LaunchedEffect(phoneNumberState) {
        snapshotFlow { phoneNumberState.text.toString() }.collectLatest {
            buatAkunActions(BuatAkunActions.UpdateFillPhoneNumber(it))
        }
    }

    val addressState = rememberTextFieldState(buatAkunUiState.address)
    LaunchedEffect(addressState) {
        snapshotFlow { addressState.text.toString() }.collectLatest {
            buatAkunActions(BuatAkunActions.UpdateFillAddress(it))
        }
    }

    val newPasswordState = rememberTextFieldState(buatAkunUiState.newPassword)
    LaunchedEffect(newPasswordState) {
        snapshotFlow { newPasswordState.text.toString() }.collectLatest {
            buatAkunActions(BuatAkunActions.UpdateFillNewPassword(it))
        }
    }

    val newPasswordConfirmationState = rememberTextFieldState(buatAkunUiState.confirmPassword)
    LaunchedEffect(newPasswordConfirmationState) {
        snapshotFlow { newPasswordConfirmationState.text.toString() }.collectLatest {
            buatAkunActions(BuatAkunActions.UpdateFillConfirmPassword(it))
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { contetUri ->
        if (contetUri != null) {
            buatAkunActions(BuatAkunActions.UpdateFillPhotoKtp(contetUri))
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val nameRequester = remember { BringIntoViewRequester() }
    val nameFocus = remember { FocusRequester() }

    val emailRequester = remember { BringIntoViewRequester() }
    val emailFocus = remember { FocusRequester() }

    val passwordRequester = remember { BringIntoViewRequester() }
    val passwordFocus = remember { FocusRequester() }

    val confirmPasswordRequester = remember { BringIntoViewRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }

    val nomorHpRequester = remember { BringIntoViewRequester() }
    val nomorHpFocus = remember { FocusRequester() }

    val alamatRequester = remember { BringIntoViewRequester() }
    val alamatFocus = remember { FocusRequester() }

    LaunchedEffect(
        buatAkunUiState.nameShakeTrigger,
        buatAkunUiState.emailShakeTrigger,
        buatAkunUiState.newPasswordShakeTrigger,
        buatAkunUiState.confirmPasswordShakeTrigger,
        buatAkunUiState.phoneNumberShakeTrigger,
        buatAkunUiState.addressShakeTrigger,
    ) {
        if (buatAkunUiState.isNameError) {
            delay(400)
            nameRequester.bringIntoView()
            nameFocus.requestFocus()
        }
        else if (buatAkunUiState.isEmailError) {
            delay(400)
            emailRequester.bringIntoView()
            emailFocus.requestFocus()
        }
        else if (buatAkunUiState.isNewPasswordError){
            delay(400)
            passwordRequester.bringIntoView()
            passwordFocus.requestFocus()
        }
        else if (buatAkunUiState.isConfirmPasswordError){
            delay(400)
            confirmPasswordRequester.bringIntoView()
            confirmPasswordFocus.requestFocus()
        }
        else if (buatAkunUiState.isPhoneNumberError){
            delay(400)
            nomorHpRequester.bringIntoView()
            nomorHpFocus.requestFocus()
        }
        else if (buatAkunUiState.isAddressError){
            delay(400)
            alamatRequester.bringIntoView()
            alamatFocus.requestFocus()
        }
    }

    BoxWithConstraints(
        Modifier.fillMaxSize()
            .imePadding()
    ) {
        val screenHeight = maxHeight

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
                // KUNCI: Paksa tinggi minimal setinggi layar
                .heightIn(min = screenHeight - 40.dp)
        ) {
            val (fillName, fillEmail, fillNewPassword, fillConfirmPassword, fillAddress, fillPhoneNumber, titleAndDescription, chooseRole, chooseKtp, textAddAnotherData, buttonCreate) = createRefs()

            // Title and Description
            TitleAndDescription(
                modifier = Modifier.constrainAs(titleAndDescription) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            // TextField Name
            GeneralTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(fillName) {
                        top.linkTo(titleAndDescription.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .bringIntoViewRequester(nameRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                nameRequester.bringIntoView()
                            }
                        }
                    },
                state = nameState,
                isError = buatAkunUiState.isNameError,
                error = buatAkunUiState.nameError?.asString() ?: "",
                label = R.string.name,
                shakeTrigger = buatAkunUiState.nameShakeTrigger,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                focusRequester = nameFocus
            )

            // TextField Email
            GeneralTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(fillEmail) {
                        top.linkTo(fillName.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .bringIntoViewRequester(emailRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                emailRequester.bringIntoView()
                            }
                        }
                    },
                state = emailState,
                isError = buatAkunUiState.isEmailError,
                error = buatAkunUiState.emailError?.asString() ?: "",
                label = R.string.email,
                shakeTrigger = buatAkunUiState.emailShakeTrigger,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                focusRequester = emailFocus
            )

            // TextField New Password
            PasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(fillNewPassword) {
                        top.linkTo(fillEmail.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .bringIntoViewRequester(passwordRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                passwordRequester.bringIntoView()
                            }
                        }
                    },
                state = newPasswordState,
                isError = buatAkunUiState.isNewPasswordError,
                error = buatAkunUiState.newPasswordError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password,
                ),
                shakeTrigger = buatAkunUiState.newPasswordShakeTrigger,
                label = R.string.password,
                focusRequester = passwordFocus
            )

            // TextField Confirm Password
            PasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(fillConfirmPassword) {
                        top.linkTo(fillNewPassword.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .bringIntoViewRequester(confirmPasswordRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(500)
                                confirmPasswordRequester.bringIntoView()
                            }
                        }
                    },
                state = newPasswordConfirmationState,
                isError = buatAkunUiState.isConfirmPasswordError,
                error = buatAkunUiState.confirmPasswordError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                ),
                shakeTrigger = buatAkunUiState.confirmPasswordShakeTrigger,
                label = R.string.rewrite_password,
                focusRequester = confirmPasswordFocus
            )

            // Choose Role
            ChooseRoleSection(
                modifier = Modifier
                    .constrainAs(chooseRole) {
                        top.linkTo(fillConfirmPassword.bottom, margin = 15.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                buatAkunUiState = buatAkunUiState,
                buatAkunActions = buatAkunActions
            )

            // Kondisi Check Role Penghuni untuk add data lain
            if (buatAkunUiState.role == "penghuni") {
                // textAddAnotherData
                Text(
                    text = stringResource(R.string.another_need_data_for_role_penghuni),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.constrainAs(textAddAnotherData) {
                        top.linkTo(chooseRole.bottom, margin = 15.dp)
                        start.linkTo(parent.start)
                    }
                )

                // TextField NoHp
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(nomorHpRequester)
                        .constrainAs(fillPhoneNumber) {
                            top.linkTo(textAddAnotherData.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    nomorHpRequester.bringIntoView()
                                }
                            }
                        },
                    state = phoneNumberState,
                    isError = buatAkunUiState.isPhoneNumberError,
                    error = buatAkunUiState.phoneNumberError?.asString() ?: "",
                    label = R.string.phone_number,
                    shakeTrigger = buatAkunUiState.phoneNumberShakeTrigger,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Phone,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    inputTransformation = InputTransformation.maxLength(13),
                    focusRequester = nomorHpFocus
                )

                // TextField Address
                GeneralTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(alamatRequester)
                        .constrainAs(fillAddress) {
                            top.linkTo(fillPhoneNumber.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .onFocusEvent { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    delay(500)
                                    alamatRequester.bringIntoView()
                                }
                            }
                        },
                    state = addressState,
                    isError = buatAkunUiState.isAddressError,
                    error = buatAkunUiState.addressError?.asString() ?: "",
                    label = R.string.address,
                    shakeTrigger = buatAkunUiState.addressShakeTrigger,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    focusRequester = alamatFocus
                )

                // Choose Ktp
                BuktiFotoCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(chooseKtp) {
                            top.linkTo(fillAddress.bottom, margin = 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    previewOnly = false,
                    title = stringResource(R.string.title_not_fill_ktp),
                    description = stringResource(R.string.description_not_fill_ktp),
                    imageUri = buatAkunUiState.photoKtp,
                    isCanChooseImage = true,
                    isBuktiLaporan = false,
                    onPreviewImage = {
                        buatAkunActions(
                            BuatAkunActions
                                .NavigateToPreviewImage(
                                    buatAkunUiState.photoKtp
                                )
                        )
                    },
                    onChooseImage = {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }

            ActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(buttonCreate) {
                        val anchorTop =
                            if (buatAkunUiState.role == "penghuni") chooseKtp.bottom else chooseRole.bottom
                        top.linkTo(anchorTop, margin = 20.dp)
                        bottom.linkTo(parent.bottom)
                        verticalBias = 1f
                        centerHorizontallyTo(parent)
                    },
                height = 43.dp,
                onClick = {
                    keyboardController?.hide()
                    buatAkunActions(BuatAkunActions.AddAkun)
                          },
                text = stringResource(R.string.create_account),
                shape = RoundedCornerShape(12.dp),
                isLoading = buatAkunUiState.isButtonLoading
            )
        }
    }
}

@Composable
private fun TitleAndDescription(
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.title_create_account),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.description_create_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ChooseRoleSection(
    modifier: Modifier = Modifier,
    buatAkunUiState: BuatAkunUiState,
    buatAkunActions: (BuatAkunActions) -> Unit
) {
    val roleOptions = listOf("admin", "penghuni")
    Column(
        modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.choose_role_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            roleOptions.forEach { text ->
                CustomRadioButtonV1(
                    modifier = Modifier.weight(1f),
                    text = text.capitalize(Locale.current),
                    selected = text == buatAkunUiState.role,
                    onClick = {
                        buatAkunActions(BuatAkunActions.UpdateSelectRole(text))
                    }
                )
            }
        }
    }
}