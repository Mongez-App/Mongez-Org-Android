package com.iti.mongez.org.designsystem.foundation.color

import androidx.compose.ui.graphics.Color

/**
 * Raw color palette — internal to the design system.
 * Feature modules must NEVER access these directly.
 * Use [com.iti.mongez.org.designsystem.theme.Theme.colorScheme] instead.
 */
internal object PrimitiveColors {
    // region Specific App Shades
    // Pair 1: Primary
    val Purple200 = Color(0xFFDDD6FE)
    val PurpleDark = Color(0xFFA691E3)

    // Pair 2: Text Primary
    val Black100 = Color(0xFF101828)
    val GrayLightDark = Color(0xFFAFB0BD)

    // Pair 3: Background
    val White = Color(0xFFF9F9FF)
    val BlackDark = Color(0xFF04001B)

    // Pair 4: Borders
    val Gray350 = Color(0xFFD1D5DB)
    val GrayBorderDark = Color(0xFFB4BACD)

    // Pair 5: Text Secondary
    val Gray450 = Color(0xFFBCBCBE)
    val GraySecondaryDark = Color(0xFFB6ACCD)

    // Pair 6: Text Tertiary
    val GrayTertiaryLight = Color(0xFFB0B0B3)
    val GrayTertiaryDark = Color(0xFFB6ACCD)

    // Pair 7: Blue
    val Blue500 = Color(0xFF3B82F6)
    val BlueDark = Color(0xFF959FF7)

    // Pair 8: Green
    val Green500 = Color(0xFF10B981)
    val GreenDark = Color(0xFF10B981)

    // Pair 9: Purple (Roadmap)
    val PurpleRoadmapLight = Color(0xFFA855F7)
    val PurpleRoadmapDark = Color(0xFFC15AF3)

    // Pair 10: Red
    val Red500 = Color(0xFFEF4444)
    val RedDark = Color(0xFFEF4444)

    // Pair 11: Error Container/Variant
    val ErrorLight = Color(0xFFEF4444)
    val ErrorDark = Color(0xFFF1CB84)

    // Pair 12: Orange
    val OrangeRoadmapLight = Color(0xFFF1CB84)
    val OrangeRoadmapDark = Color(0xFFF6B576)
    // endregion
}
