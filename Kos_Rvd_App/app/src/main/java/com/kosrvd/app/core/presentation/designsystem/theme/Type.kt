package com.kosrvd.app.core.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.kosrvd.app.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyTitleFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Open Sans"),
        fontProvider = provider,
    )
)

val labelFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Roboto"),
        fontProvider = provider,
    )
)

val displayHeadlineFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayHeadlineFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayHeadlineFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayHeadlineFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayHeadlineFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayHeadlineFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayHeadlineFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = bodyTitleFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = bodyTitleFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = bodyTitleFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyTitleFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyTitleFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyTitleFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = labelFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = labelFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = labelFontFamily),
)