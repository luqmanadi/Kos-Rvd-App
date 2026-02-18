package com.kosrvd.app.core.data.constant

object FirebaseAuthErrors {
    // Common errors yang sering terjadi di Firebase Authentication
    const val USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
    const val USER_DISABLED = "ERROR_USER_DISABLED"
    const val EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
    const val INVALID_CREDENTIAL = "ERROR_INVALID_CREDENTIAL"
    const val TOKEN_EXPIRED = "ERROR_USER_TOKEN_EXPIRED "
    const val INVALID_TOKEN = "ERROR_INVALID_USER_TOKEN"
    const val WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"

    // Less common but good to have
    const val ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL = "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL"
    const val CREDENTIAL_ALREADY_IN_USE = "ERROR_CREDENTIAL_ALREADY_IN_USE"
    const val ERROR_REQUIRES_RECENT_LOGIN = "ERROR_REQUIRES_RECENT_LOGIN"
}