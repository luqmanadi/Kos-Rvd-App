package com.kosrvd.app.feature.profile.presentation.edit_data_profile

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kosrvd.app.R
import com.kosrvd.app.core.domain.utils.CustomToastHostState
import com.kosrvd.app.presentation.navigation.models.TemporaryData
import com.kosrvd.app.core.presentation.designsystem.component.appbar.TopBarLeftTitle
import com.kosrvd.app.core.presentation.designsystem.component.button.ActionButton
import com.kosrvd.app.core.presentation.designsystem.component.text.CustomToastHost
import com.kosrvd.app.core.presentation.designsystem.component.textfield.GeneralTextField
import com.kosrvd.app.core.presentation.designsystem.component.textfield.PasswordTextField
import com.kosrvd.app.feature.profile.domain.utils.TypeEdit
import com.kosrvd.app.feature.profile.presentation.component.DialogInputOldPassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun EditDataProfileScreen(
    editDataProfileUiState: EditDataProfileUiState,
    editDataProfileActions : (EditDataProfileActions) -> Unit,
    typeEdit: TypeEdit,
    customToastHostState: CustomToastHostState,
    temporaryData : TemporaryData?
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val oldName = temporaryData?.name ?: ""
    val oldEmail = temporaryData?.email ?: ""
    val oldPhoneNumber = temporaryData?.phoneNumber ?: ""
    val oldAddress = temporaryData?.address ?: ""

    val titleTopBar = when(typeEdit){
        TypeEdit.NAMA -> stringResource(R.string.edit_name)
        TypeEdit.EMAIL -> stringResource(R.string.edit_email)
        TypeEdit.GANTI_PASSWORD -> stringResource(R.string.change_password)
        TypeEdit.NO_HP -> stringResource(R.string.edit_phone_number)
        TypeEdit.ALAMAT -> stringResource(R.string.edit_address)
    }

    val nameState = rememberTextFieldState(editDataProfileUiState.name)
    LaunchedEffect(nameState) {
        snapshotFlow { nameState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdateName(it))
        }
    }

    val emailState = rememberTextFieldState(editDataProfileUiState.email)
    LaunchedEffect(emailState) {
        snapshotFlow { emailState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdateEmail(it))
        }
    }

    val phoneNumberState = rememberTextFieldState(editDataProfileUiState.phoneNumber)
    LaunchedEffect(phoneNumberState) {
        snapshotFlow { phoneNumberState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdatePhoneNumber(it))
        }
    }

    val addressState = rememberTextFieldState(editDataProfileUiState.address)
    LaunchedEffect(addressState) {
        snapshotFlow { addressState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdateAddress(it))
        }
    }

    val oldPasswordState = rememberTextFieldState(editDataProfileUiState.oldPassword)
    LaunchedEffect(oldPasswordState) {
        snapshotFlow { oldPasswordState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdateOldPassword(it))
        }
    }

    val newPasswordState = rememberTextFieldState(editDataProfileUiState.newPassword)
    LaunchedEffect(newPasswordState) {
        snapshotFlow { newPasswordState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdatePassword(it))
        }
    }

    val newPasswordConfirmationState = rememberTextFieldState(editDataProfileUiState.newPasswordConfirmation)
    LaunchedEffect(newPasswordConfirmationState) {
        snapshotFlow { newPasswordConfirmationState.text.toString() }.collectLatest {
            editDataProfileActions(EditDataProfileActions.UpdatePasswordConfirmation(it))
        }
    }

    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val nameRequester = remember { BringIntoViewRequester() }
    val nameFocus = remember { FocusRequester() }

    val emailRequester = remember { BringIntoViewRequester() }
    val emailFocus = remember { FocusRequester() }

    val oldPasswordRequester = remember { BringIntoViewRequester() }
    val oldPasswordFocus = remember { FocusRequester() }

    val newPasswordRequester = remember { BringIntoViewRequester() }
    val newPasswordFocus = remember { FocusRequester() }

    val confirmPasswordRequester = remember { BringIntoViewRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }

    val nomorHpRequester = remember { BringIntoViewRequester() }
    val nomorHpFocus = remember { FocusRequester() }

    val alamatRequester = remember { BringIntoViewRequester() }
    val alamatFocus = remember { FocusRequester() }

    LaunchedEffect(
        editDataProfileUiState.shakeTriggerResponseError,
        editDataProfileUiState.shakeTriggerNewPasswordError,
        editDataProfileUiState.shakeTriggerOldPasswordError
    ) {
        if (editDataProfileUiState.isNameError) {
            listState.animateScrollToItem(2)
            delay(400)
            nameRequester.bringIntoView()
            nameFocus.requestFocus()
        }
        else if (editDataProfileUiState.isEmailError) {
            listState.animateScrollToItem(2)
            delay(400)
            emailRequester.bringIntoView()
            emailFocus.requestFocus()
        }
        else if (editDataProfileUiState.isOldPasswordError){
            listState.animateScrollToItem(2)
            delay(400)
            oldPasswordRequester.bringIntoView()
            oldPasswordFocus.requestFocus()
        }
        else if (editDataProfileUiState.isNewPasswordError){
            listState.animateScrollToItem(6)
            delay(400)
            newPasswordRequester.bringIntoView()
            newPasswordFocus.requestFocus()
        }
        else if (editDataProfileUiState.isNewPasswordConfirmationError){
            listState.animateScrollToItem(8)
            delay(400)
            confirmPasswordRequester.bringIntoView()
            confirmPasswordFocus.requestFocus()
        }
        else if (editDataProfileUiState.isPhoneNumberError){
            listState.animateScrollToItem(2)
            delay(400)
            nomorHpRequester.bringIntoView()
            nomorHpFocus.requestFocus()
        }
        else if (editDataProfileUiState.isAddressError){
            listState.animateScrollToItem(2)
            delay(400)
            alamatRequester.bringIntoView()
            alamatFocus.requestFocus()
        }
    }


    Scaffold(
        topBar = {
            TopBarLeftTitle(
                title = titleTopBar,
                onBackClick = {
                    keyboardController?.hide()
                    editDataProfileActions(EditDataProfileActions.NavigateBack)
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
                    .imePadding(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp)
            ) {
                when(typeEdit){
                    TypeEdit.NAMA -> contentUpdateName(
                        nameState = nameState,
                        editDataProfileUiState = editDataProfileUiState,
                        oldName = oldName,
                        editDataProfileActions = editDataProfileActions,
                        keyboardController = keyboardController,
                        nameRequester = nameRequester,
                        nameFocus = nameFocus,
                        coroutineScope = coroutineScope,
                    )
                    TypeEdit.EMAIL -> contentUpdateEmail(
                        emailState = emailState,
                        editDataProfileUiState = editDataProfileUiState,
                        oldEmaild = oldEmail,
                        editDataProfileActions = editDataProfileActions,
                        keyboardController = keyboardController,
                        emailRequester = emailRequester,
                        emailFocus = emailFocus,
                        coroutineScope = coroutineScope,
                    )
                    TypeEdit.GANTI_PASSWORD -> contentChangePassword(
                        oldPasswordState = oldPasswordState,
                        newPasswordState = newPasswordState,
                        newPasswordConfirmationState = newPasswordConfirmationState,
                        editDataProfileUiState = editDataProfileUiState,
                        editDataProfileActions = editDataProfileActions,
                        keyboardController = keyboardController,
                        oldPasswordRequester = oldPasswordRequester,
                        oldPasswordFocus = oldPasswordFocus,
                        newPasswordRequester = newPasswordRequester,
                        newPasswordFocus = newPasswordFocus,
                        confirmPasswordRequester = confirmPasswordRequester,
                        confirmPasswordFocus = confirmPasswordFocus,
                        coroutineScope = coroutineScope,
                    )
                    TypeEdit.NO_HP -> contentUpdatePhoneNumber(
                        phoneNumberState = phoneNumberState,
                        editDataProfileUiState = editDataProfileUiState,
                        oldPhoneNumber = oldPhoneNumber,
                        editDataProfileActions = editDataProfileActions,
                        keyboardController = keyboardController,
                        nomorHpRequester = nomorHpRequester,
                        nomorHpFocus = nomorHpFocus,
                        coroutineScope = coroutineScope,
                    )
                    TypeEdit.ALAMAT -> contentUpdateAddress(
                        addressState = addressState,
                        editDataProfileUiState = editDataProfileUiState,
                        oldAddress = oldAddress,
                        editDataProfileActions = editDataProfileActions,
                        keyboardController = keyboardController,
                        alamatRequester = alamatRequester,
                        alamatFocus = alamatFocus,
                        coroutineScope = coroutineScope,
                    )
                }

            }

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

        if (editDataProfileUiState.showDialogConfirmationPassword){
            val keyboardControllerInDialogConfirmation = LocalSoftwareKeyboardController.current
            val oldPasswordStateInDialogConfirmation = rememberTextFieldState(editDataProfileUiState.oldPassword)
            LaunchedEffect(oldPasswordStateInDialogConfirmation) {
                snapshotFlow { oldPasswordStateInDialogConfirmation.text.toString() }.collectLatest {
                    editDataProfileActions(EditDataProfileActions.UpdateOldPassword(it))
                }
            }

            DialogInputOldPassword(
                textFieldState = oldPasswordStateInDialogConfirmation,
                isError = editDataProfileUiState.isOldPasswordError,
                errorMessage = editDataProfileUiState.oldPasswordError,
                shakeTrigger = editDataProfileUiState.shakeTriggerOldPasswordError,
                isLoading = editDataProfileUiState.isButtonLoading,
                onDismiss = {
                    keyboardControllerInDialogConfirmation?.hide()
                    editDataProfileActions(EditDataProfileActions.DismissDialogConfirmationPassword)
                            },
                onConfirm = {
                    keyboardControllerInDialogConfirmation?.hide()
                    editDataProfileActions(EditDataProfileActions.EditEmail)
                }
            )
        }
    }
}

fun LazyListScope.contentUpdateName(
    nameState: TextFieldState,
    editDataProfileUiState: EditDataProfileUiState,
    editDataProfileActions: (EditDataProfileActions) -> Unit,
    keyboardController:  SoftwareKeyboardController?,
    oldName: String,
    nameRequester: BringIntoViewRequester,
    nameFocus: FocusRequester,
    coroutineScope: CoroutineScope
){
    item(key = "Masukkan Nama Header") {
        Text(
            text = stringResource(R.string.enter_new_name),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Input Nama Field") {
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
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
            isError = editDataProfileUiState.isNameError,
            error = editDataProfileUiState.nameError?.asString() ?: "",
            label = R.string.name,
            shakeTrigger = editDataProfileUiState.shakeTriggerResponseError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            focusRequester = nameFocus
        )
    }
    item { Spacer(Modifier.height(20.dp)) }
    item(key = "Button Simpan") {
        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            height = 43.dp,
            onClick = {
                keyboardController?.hide()
                editDataProfileActions(EditDataProfileActions.EditName)
                      },
            text = stringResource(R.string.save),
            shape = RoundedCornerShape(12.dp),
            isLoading = editDataProfileUiState.isButtonLoading,
            enabled = oldName != editDataProfileUiState.name,
            disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            disableContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun LazyListScope.contentUpdateEmail(
    emailState: TextFieldState,
    editDataProfileUiState: EditDataProfileUiState,
    editDataProfileActions: (EditDataProfileActions) -> Unit,
    keyboardController:  SoftwareKeyboardController?,
    oldEmaild: String,
    emailRequester: BringIntoViewRequester,
    emailFocus: FocusRequester,
    coroutineScope: CoroutineScope
){
    item(key = "Masukkan Email Header") {
        Text(
            text = stringResource(R.string.enter_new_email),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Input Email Field") {
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
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
            isError = editDataProfileUiState.isEmailError,
            error = editDataProfileUiState.emailError?.asString() ?: "",
            label = R.string.email,
            shakeTrigger = editDataProfileUiState.shakeTriggerResponseError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            focusRequester = emailFocus
        )
    }
    item { Spacer(Modifier.height(20.dp)) }
    item(key = "Button Simpan") {
        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            height = 43.dp,
            onClick = {
                keyboardController?.hide()
                editDataProfileActions(EditDataProfileActions.OpenDialogConfirmationPassword)
                      },
            text = stringResource(R.string.save),
            shape = RoundedCornerShape(12.dp),
            enabled = oldEmaild != editDataProfileUiState.email,
            disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            disableContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun LazyListScope.contentChangePassword(
    oldPasswordState: TextFieldState,
    newPasswordState: TextFieldState,
    newPasswordConfirmationState: TextFieldState,
    editDataProfileUiState: EditDataProfileUiState,
    editDataProfileActions: (EditDataProfileActions) -> Unit,
    keyboardController:  SoftwareKeyboardController?,
    oldPasswordRequester: BringIntoViewRequester,
    oldPasswordFocus: FocusRequester,
    newPasswordRequester: BringIntoViewRequester,
    newPasswordFocus: FocusRequester,
    confirmPasswordRequester: BringIntoViewRequester,
    confirmPasswordFocus: FocusRequester,
    coroutineScope: CoroutineScope
){
    item(key = "Masukkan Password Lama Header") {
        Text(
            text = stringResource(R.string.enter_old_password),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Input Old Password Field") {
        PasswordTextField(
            modifier = Modifier.fillMaxWidth()
                .bringIntoViewRequester(oldPasswordRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            oldPasswordRequester.bringIntoView()
                        }
                    }
                },
            state = oldPasswordState,
            isError = editDataProfileUiState.isOldPasswordError,
            error = editDataProfileUiState.oldPasswordError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
            ),
            shakeTrigger = editDataProfileUiState.shakeTriggerOldPasswordError,
            label = R.string.enter_old_password,
            focusRequester = oldPasswordFocus
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Masukkan Password Header") {
        Text(
            text = stringResource(R.string.enter_new_password),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Input New Password Field") {
        PasswordTextField(
            modifier = Modifier.fillMaxWidth()
                .bringIntoViewRequester(newPasswordRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            newPasswordRequester.bringIntoView()
                        }
                    }
                },
            state = newPasswordState,
            isError = editDataProfileUiState.isNewPasswordError,
            error = editDataProfileUiState.newPasswordError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
            ),
            shakeTrigger = editDataProfileUiState.shakeTriggerNewPasswordError,
            label = R.string.enter_new_password,
            focusRequester = newPasswordFocus
        )
    }
    item { Spacer(Modifier.height(10.dp)) }
    item(key = "Input New Password Confirmation Field") {
        PasswordTextField(
            modifier = Modifier.fillMaxWidth()
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
            isError = editDataProfileUiState.isNewPasswordConfirmationError,
            error = editDataProfileUiState.newPasswordConfirmationError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
            ),
            shakeTrigger = editDataProfileUiState.shakeTriggerResponseError,
            label = R.string.rewrite_new_password,
            focusRequester = confirmPasswordFocus
        )
    }
    item { Spacer(Modifier.height(20.dp)) }
    item(key = "Button Simpan") {
        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            height = 43.dp,
            onClick = {
                keyboardController?.hide()
                editDataProfileActions(EditDataProfileActions.EditPassword)
                      },
            text = stringResource(R.string.save),
            shape = RoundedCornerShape(12.dp),
            isLoading = editDataProfileUiState.isButtonLoading,
            disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            disableContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun LazyListScope.contentUpdatePhoneNumber(
    phoneNumberState: TextFieldState,
    editDataProfileUiState: EditDataProfileUiState,
    editDataProfileActions: (EditDataProfileActions) -> Unit,
    oldPhoneNumber: String,
    keyboardController:  SoftwareKeyboardController?,
    nomorHpRequester: BringIntoViewRequester,
    nomorHpFocus: FocusRequester,
    coroutineScope: CoroutineScope
){
    item(key = "Masukkan No Hp Header") {
        Text(
            text = stringResource(R.string.enter_new_phone_number),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Input No Hp Field") {
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
                .bringIntoViewRequester(nomorHpRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            nomorHpRequester.bringIntoView()
                        }
                    }
                },
            state = phoneNumberState,
            isError = editDataProfileUiState.isPhoneNumberError,
            error = editDataProfileUiState.phoneNumberError?.asString() ?: "",
            label = R.string.phone_number,
            shakeTrigger = editDataProfileUiState.shakeTriggerResponseError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone,
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            inputTransformation = InputTransformation.maxLength(13),
            focusRequester = nomorHpFocus
        )
    }
    item { Spacer(Modifier.height(20.dp)) }
    item(key = "Button Simpan") {
        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            height = 43.dp,
            onClick = {
                keyboardController?.hide()
                editDataProfileActions(EditDataProfileActions.EditPhoneNumber)
                      },
            text = stringResource(R.string.save),
            shape = RoundedCornerShape(12.dp),
            isLoading = editDataProfileUiState.isButtonLoading,
            enabled = oldPhoneNumber != editDataProfileUiState.phoneNumber,
            disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            disableContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

fun LazyListScope.contentUpdateAddress(
    addressState: TextFieldState,
    editDataProfileUiState: EditDataProfileUiState,
    editDataProfileActions: (EditDataProfileActions) -> Unit,
    oldAddress: String,
    keyboardController:  SoftwareKeyboardController?,
    alamatRequester: BringIntoViewRequester,
    alamatFocus: FocusRequester,
    coroutineScope: CoroutineScope
){
    item(key = "Masukkan Alamat Baru Header") {
        Text(
            text = stringResource(R.string.enter_new_address),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    item { Spacer(Modifier.height(15.dp)) }
    item(key = "Input Alamat Baru Field") {
        GeneralTextField(
            modifier = Modifier.fillMaxWidth()
                .bringIntoViewRequester(alamatRequester)
                .onFocusEvent { focusState ->
                    if (focusState.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            alamatRequester.bringIntoView()
                        }
                    }
                },
            state = addressState,
            isError = editDataProfileUiState.isAddressError,
            error = editDataProfileUiState.addressError?.asString() ?: "",
            label = R.string.address,
            shakeTrigger = editDataProfileUiState.shakeTriggerResponseError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            focusRequester = alamatFocus
        )
    }
    item { Spacer(Modifier.height(20.dp)) }
    item(key = "Button Simpan") {
        ActionButton(
            modifier = Modifier.fillMaxWidth(),
            height = 43.dp,
            onClick = {
                keyboardController?.hide()
                editDataProfileActions(EditDataProfileActions.EditAddress)
                      },
            text = stringResource(R.string.save),
            shape = RoundedCornerShape(12.dp),
            isLoading = editDataProfileUiState.isButtonLoading,
            enabled = oldAddress != editDataProfileUiState.address,
            disableContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            disableContentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}