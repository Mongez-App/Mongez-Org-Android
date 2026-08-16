package com.iti.mongez.org.designsystem.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Raw color palette — internal to the design system.
 * Feature modules must NEVER access these directly.
 * Use [com.iti.mongez.org.designsystem.theme.Theme.colorScheme] instead.
 */
internal object PrimitiveColors {
    // region Corrected App Shades
    // Primary
    val PrimaryLight = Color(0xFF5A67D8)
    val PrimaryDark = Color(0xFFA691E3)

    // Text Primary
    val TextPrimaryLight = Color(0xFF0F172A)
    val TextPrimaryDark = Color(0xFFAFB0BD)

    // Background
    val BackgroundLight = Color(0xFFF9F9FF)
    val BackgroundDark = Color(0xFF04001B)

    // Border
    val BorderLight = Color(0xFFD1D5DB)
    val BorderDark = Color(0xFFB4BACD)

    // Text Secondary
    val TextSecondaryLight = Color(0xFFBCBCBE)
    val TextSecondaryDark = Color(0xFFB6ACCD)

    // Info
    val InfoLight = Color(0xFF3B82F6)
    val InfoDark = Color(0xFF959FF7)

    // Success
    val SuccessLight = Color(0xFF10B981)
    val SuccessDark = Color(0xFF10B981)

    // Error
    val ErrorLight = Color(0xFFEF4444)
    val ErrorDark = Color(0xFFEF4444)

    // Warning
    val WarningLight = Color(0xFFF1CB84)
    val WarningDark = Color(0xFFF6B576)
    // endregion
}
