package com.bigbratan.emulair.ui.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.bigbratan.emulair.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val plusJakartaSans = FontFamily(
    Font(
        googleFont = GoogleFont("Plus Jakarta Sans"),
        fontProvider = provider,
    )
)

val removeFontPadding = PlatformTextStyle(
    includeFontPadding = false,
)

