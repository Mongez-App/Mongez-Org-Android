package com.iti.mongez.org.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class BrandColors(
    val primary: Color,
    val primaryVariant: Color,
    val primaryContainer: Color,
    val onPrimary: Color,
    val onPrimaryContainer: Color,
    val indicatorUnselected: Color,
    val roadmapPurple: Color,
    val roadmapGreen: Color,
    val roadmapOrange: Color,
    val roadmapBlue: Color,
    val roadmapTimeline: Color,
)

@Immutable
data class TextColors(
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val hint: Color,
    val disabled: Color,
    val inverse: Color,
    val dialogLabel: Color,
)

@Immutable
data class SurfaceColors(
    val background: Color,
    val surface: Color,
    val surfaceLow: Color,
    val surfaceVariant: Color,
    val surfaceContainer: Color,
    val surfaceHigh: Color,
    val surfaceHighest: Color,
)

@Immutable
data class BorderColors(
    val primary: Color,
    val secondary: Color,
    val focused: Color,
    val error: Color,
    val success: Color,
    val disabled: Color,
)

@Immutable
data class StateColors(
    val success: Color,
    val successContainer: Color,
    val warning: Color,
    val warningContainer: Color,
    val error: Color,
    val errorContainer: Color,
    val info: Color,
    val infoContainer: Color,
)

@Immutable
data class ButtonColors(
    val primaryBackground: Color,
    val primaryContent: Color,
    val secondaryBackground: Color,
    val secondaryContent: Color,
    val secondaryBorder: Color,
    val disabledBackground: Color,
    val disabledContent: Color,
)

@Immutable
data class InputColors(
    val background: Color,
    val text: Color,
    val placeholder: Color,
    val border: Color,
    val focusedBorder: Color,
    val errorBorder: Color,
    val icon: Color,
)

@Immutable
data class ChipColors(
    val selectedBackground: Color,
    val selectedContent: Color,
    val unselectedBackground: Color,
    val unselectedContent: Color,
    val unselectedBorder: Color,
)

@Immutable
data class NavigationColors(
    val background: Color,
    val activeIcon: Color,
    val activeLabel: Color,
    val inactiveIcon: Color,
    val inactiveLabel: Color,
    val indicator: Color,
)

@Immutable
data class CardColors(
    val background: Color,
    val border: Color,
)

@Immutable
data class EventColors(
    val studyContainer: Color,
    val studyIcon: Color,
    val assignmentContainer: Color,
    val assignmentIcon: Color,
    val quizContainer: Color,
    val quizIcon: Color,
    val examContainer: Color,
    val examIcon: Color,
    val projectContainer: Color,
    val projectIcon: Color,
)

@Immutable
data class AppColorScheme(
    val brand: BrandColors,
    val text: TextColors,
    val surface: SurfaceColors,
    val border: BorderColors,
    val state: StateColors,
    val button: ButtonColors,
    val input: InputColors,
    val chip: ChipColors,
    val navigation: NavigationColors,
    val card: CardColors,
    val events: EventColors,
)

// ──────────────────────────────────────────────────────────────
// Light Theme
// ──────────────────────────────────────────────────────────────
val LightColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.PrimaryLight,
        primaryVariant = PrimitiveColors.PrimaryLight,
        primaryContainer = PrimitiveColors.PrimaryLight.copy(alpha = 0.1f),
        onPrimary = Color.White,
        onPrimaryContainer = PrimitiveColors.PrimaryLight,
        indicatorUnselected = PrimitiveColors.BorderLight,
        roadmapPurple = PrimitiveColors.PrimaryLight,
        roadmapGreen = PrimitiveColors.SuccessLight,
        roadmapOrange = PrimitiveColors.WarningLight,
        roadmapBlue = PrimitiveColors.InfoLight,
        roadmapTimeline = PrimitiveColors.BorderLight,
    ),
    text = TextColors(
        primary = PrimitiveColors.TextPrimaryLight,
        secondary = PrimitiveColors.TextSecondaryLight,
        tertiary = PrimitiveColors.TextSecondaryLight,
        hint = PrimitiveColors.TextSecondaryLight,
        disabled = PrimitiveColors.BorderLight,
        inverse = Color.White,
        dialogLabel = PrimitiveColors.TextSecondaryLight,
    ),
    surface = SurfaceColors(
        background = PrimitiveColors.BackgroundLight,
        surface = Color.White,
        surfaceLow = PrimitiveColors.BackgroundLight,
        surfaceVariant = PrimitiveColors.BackgroundLight,
        surfaceContainer = PrimitiveColors.BackgroundLight,
        surfaceHigh = PrimitiveColors.BackgroundLight,
        surfaceHighest = PrimitiveColors.BackgroundLight,
    ),
    border = BorderColors(
        primary = PrimitiveColors.BorderLight,
        secondary = PrimitiveColors.BorderLight,
        focused = PrimitiveColors.PrimaryLight,
        error = PrimitiveColors.ErrorLight,
        success = PrimitiveColors.SuccessLight,
        disabled = PrimitiveColors.BorderLight,
    ),
    state = StateColors(
        success = PrimitiveColors.SuccessLight,
        successContainer = PrimitiveColors.SuccessLight.copy(alpha = 0.1f),
        warning = PrimitiveColors.WarningLight,
        warningContainer = PrimitiveColors.WarningLight.copy(alpha = 0.1f),
        error = PrimitiveColors.ErrorLight,
        errorContainer = PrimitiveColors.ErrorLight.copy(alpha = 0.1f),
        info = PrimitiveColors.InfoLight,
        infoContainer = PrimitiveColors.InfoLight.copy(alpha = 0.1f),
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.PrimaryLight,
        primaryContent = Color.White,
        secondaryBackground = Color.White,
        secondaryContent = PrimitiveColors.PrimaryLight,
        secondaryBorder = PrimitiveColors.PrimaryLight,
        disabledBackground = PrimitiveColors.BorderLight,
        disabledContent = Color.White,
    ),
    input = InputColors(
        background = Color.White,
        text = PrimitiveColors.TextPrimaryLight,
        placeholder = PrimitiveColors.TextSecondaryLight,
        border = PrimitiveColors.BorderLight,
        focusedBorder = PrimitiveColors.PrimaryLight,
        errorBorder = PrimitiveColors.ErrorLight,
        icon = PrimitiveColors.TextSecondaryLight,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.PrimaryLight,
        selectedContent = Color.White,
        unselectedBackground = Color.White,
        unselectedContent = PrimitiveColors.TextSecondaryLight,
        unselectedBorder = PrimitiveColors.BorderLight,
    ),
    navigation = NavigationColors(
        background = Color.White,
        activeIcon = PrimitiveColors.PrimaryLight,
        activeLabel = PrimitiveColors.PrimaryLight,
        inactiveIcon = PrimitiveColors.TextSecondaryLight,
        inactiveLabel = PrimitiveColors.TextSecondaryLight,
        indicator = PrimitiveColors.PrimaryLight,
    ),
    card = CardColors(
        background = Color.White,
        border = PrimitiveColors.BorderLight,
    ),
    events = EventColors(
        studyContainer = PrimitiveColors.PrimaryLight.copy(alpha = 0.1f),
        studyIcon = PrimitiveColors.PrimaryLight,
        assignmentContainer = PrimitiveColors.WarningLight.copy(alpha = 0.1f),
        assignmentIcon = PrimitiveColors.WarningLight,
        quizContainer = PrimitiveColors.InfoLight.copy(alpha = 0.1f),
        quizIcon = PrimitiveColors.InfoLight,
        examContainer = PrimitiveColors.ErrorLight.copy(alpha = 0.1f),
        examIcon = PrimitiveColors.ErrorLight,
        projectContainer = PrimitiveColors.SuccessLight.copy(alpha = 0.1f),
        projectIcon = PrimitiveColors.SuccessLight,
    ),
)

// ──────────────────────────────────────────────────────────────
// Dark Theme
// ──────────────────────────────────────────────────────────────
val DarkColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.PrimaryDark,
        primaryVariant = PrimitiveColors.PrimaryDark,
        primaryContainer = PrimitiveColors.PrimaryDark.copy(alpha = 0.15f),
        onPrimary = Color.White,
        onPrimaryContainer = PrimitiveColors.PrimaryDark,
        indicatorUnselected = PrimitiveColors.BorderDark,
        roadmapPurple = PrimitiveColors.PrimaryDark,
        roadmapGreen = PrimitiveColors.SuccessDark,
        roadmapOrange = PrimitiveColors.WarningDark,
        roadmapBlue = PrimitiveColors.InfoDark,
        roadmapTimeline = PrimitiveColors.BorderDark,
    ),
    text = TextColors(
        primary = PrimitiveColors.TextPrimaryDark,
        secondary = PrimitiveColors.TextSecondaryDark,
        tertiary = PrimitiveColors.TextSecondaryDark,
        hint = PrimitiveColors.TextSecondaryDark,
        disabled = PrimitiveColors.BorderDark,
        inverse = PrimitiveColors.BackgroundDark,
        dialogLabel = PrimitiveColors.TextSecondaryDark,
    ),
    surface = SurfaceColors(
        background = PrimitiveColors.BackgroundDark,
        surface = PrimitiveColors.BackgroundDark,
        surfaceLow = PrimitiveColors.BackgroundDark,
        surfaceVariant = PrimitiveColors.BackgroundDark,
        surfaceContainer = PrimitiveColors.BackgroundDark,
        surfaceHigh = PrimitiveColors.BackgroundDark,
        surfaceHighest = PrimitiveColors.BackgroundDark,
    ),
    border = BorderColors(
        primary = PrimitiveColors.BorderDark,
        secondary = PrimitiveColors.BorderDark,
        focused = PrimitiveColors.PrimaryDark,
        error = PrimitiveColors.ErrorDark,
        success = PrimitiveColors.SuccessDark,
        disabled = PrimitiveColors.BorderDark,
    ),
    state = StateColors(
        success = PrimitiveColors.SuccessDark,
        successContainer = PrimitiveColors.SuccessDark.copy(alpha = 0.15f),
        warning = PrimitiveColors.WarningDark,
        warningContainer = PrimitiveColors.WarningDark.copy(alpha = 0.15f),
        error = PrimitiveColors.ErrorDark,
        errorContainer = PrimitiveColors.ErrorDark.copy(alpha = 0.15f),
        info = PrimitiveColors.InfoDark,
        infoContainer = PrimitiveColors.InfoDark.copy(alpha = 0.15f),
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.PrimaryDark,
        primaryContent = Color.White,
        secondaryBackground = PrimitiveColors.BackgroundDark,
        secondaryContent = PrimitiveColors.PrimaryDark,
        secondaryBorder = PrimitiveColors.PrimaryDark,
        disabledBackground = PrimitiveColors.BorderDark,
        disabledContent = Color.White,
    ),
    input = InputColors(
        background = PrimitiveColors.BackgroundDark,
        text = PrimitiveColors.TextPrimaryDark,
        placeholder = PrimitiveColors.TextSecondaryDark,
        border = PrimitiveColors.BorderDark,
        focusedBorder = PrimitiveColors.PrimaryDark,
        errorBorder = PrimitiveColors.ErrorDark,
        icon = PrimitiveColors.TextSecondaryDark,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.PrimaryDark,
        selectedContent = Color.White,
        unselectedBackground = PrimitiveColors.BackgroundDark,
        unselectedContent = PrimitiveColors.TextSecondaryDark,
        unselectedBorder = PrimitiveColors.BorderDark,
    ),
    navigation = NavigationColors(
        background = PrimitiveColors.BackgroundDark,
        activeIcon = PrimitiveColors.PrimaryDark,
        activeLabel = PrimitiveColors.PrimaryDark,
        inactiveIcon = PrimitiveColors.TextSecondaryDark,
        inactiveLabel = PrimitiveColors.TextSecondaryDark,
        indicator = PrimitiveColors.PrimaryDark,
    ),
    card = CardColors(
        background = PrimitiveColors.BackgroundDark,
        border = PrimitiveColors.BorderDark,
    ),
    events = EventColors(
        studyContainer = PrimitiveColors.PrimaryDark.copy(alpha = 0.15f),
        studyIcon = PrimitiveColors.PrimaryDark,
        assignmentContainer = PrimitiveColors.WarningDark.copy(alpha = 0.15f),
        assignmentIcon = PrimitiveColors.WarningDark,
        quizContainer = PrimitiveColors.InfoDark.copy(alpha = 0.15f),
        quizIcon = PrimitiveColors.InfoDark,
        examContainer = PrimitiveColors.ErrorDark.copy(alpha = 0.15f),
        examIcon = PrimitiveColors.ErrorDark,
        projectContainer = PrimitiveColors.SuccessDark.copy(alpha = 0.15f),
        projectIcon = PrimitiveColors.SuccessDark,
    ),
)
