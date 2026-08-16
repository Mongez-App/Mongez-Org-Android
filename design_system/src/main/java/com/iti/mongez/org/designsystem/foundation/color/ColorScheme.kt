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
        primary = PrimitiveColors.Purple200,
        primaryVariant = PrimitiveColors.Purple200,
        primaryContainer = PrimitiveColors.Purple200,
        onPrimary = PrimitiveColors.White,
        onPrimaryContainer = PrimitiveColors.Black100,
        indicatorUnselected = PrimitiveColors.Gray350,
        roadmapPurple = PrimitiveColors.PurpleRoadmapLight,
        roadmapGreen = PrimitiveColors.Green500,
        roadmapOrange = PrimitiveColors.OrangeRoadmapLight,
        roadmapBlue = PrimitiveColors.Blue500,
        roadmapTimeline = PrimitiveColors.Gray350,
    ),
    text = TextColors(
        primary = PrimitiveColors.Black100,
        secondary = PrimitiveColors.Gray450,
        tertiary = PrimitiveColors.GrayTertiaryLight,
        hint = PrimitiveColors.GrayTertiaryLight,
        disabled = PrimitiveColors.Gray350,
        inverse = PrimitiveColors.White,
        dialogLabel = PrimitiveColors.Gray450,
    ),
    surface = SurfaceColors(
        background = PrimitiveColors.White,
        surface = PrimitiveColors.White,
        surfaceLow = PrimitiveColors.White,
        surfaceVariant = PrimitiveColors.White,
        surfaceContainer = PrimitiveColors.White,
        surfaceHigh = PrimitiveColors.White,
        surfaceHighest = PrimitiveColors.White,
    ),
    border = BorderColors(
        primary = PrimitiveColors.Gray350,
        secondary = PrimitiveColors.Gray350,
        focused = PrimitiveColors.Purple200,
        error = PrimitiveColors.Red500,
        success = PrimitiveColors.Green500,
        disabled = PrimitiveColors.Gray350,
    ),
    state = StateColors(
        success = PrimitiveColors.Green500,
        successContainer = PrimitiveColors.Green500,
        warning = PrimitiveColors.OrangeRoadmapLight,
        warningContainer = PrimitiveColors.OrangeRoadmapLight,
        error = PrimitiveColors.Red500,
        errorContainer = PrimitiveColors.ErrorLight,
        info = PrimitiveColors.Blue500,
        infoContainer = PrimitiveColors.Blue500,
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.Purple200,
        primaryContent = PrimitiveColors.White,
        secondaryBackground = PrimitiveColors.White,
        secondaryContent = PrimitiveColors.Black100,
        secondaryBorder = PrimitiveColors.Gray350,
        disabledBackground = PrimitiveColors.Gray350,
        disabledContent = PrimitiveColors.Gray450,
    ),
    input = InputColors(
        background = PrimitiveColors.White,
        text = PrimitiveColors.Black100,
        placeholder = PrimitiveColors.GrayTertiaryLight,
        border = PrimitiveColors.Gray350,
        focusedBorder = PrimitiveColors.Purple200,
        errorBorder = PrimitiveColors.Red500,
        icon = PrimitiveColors.Gray450,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.Purple200,
        selectedContent = PrimitiveColors.White,
        unselectedBackground = PrimitiveColors.White,
        unselectedContent = PrimitiveColors.Black100,
        unselectedBorder = PrimitiveColors.Gray350,
    ),
    navigation = NavigationColors(
        background = PrimitiveColors.White,
        activeIcon = PrimitiveColors.Purple200,
        activeLabel = PrimitiveColors.Purple200,
        inactiveIcon = PrimitiveColors.Gray450,
        inactiveLabel = PrimitiveColors.Gray450,
        indicator = PrimitiveColors.Purple200,
    ),
    card = CardColors(
        background = PrimitiveColors.White,
        border = PrimitiveColors.Gray350,
    ),
    events = EventColors(
        studyContainer = PrimitiveColors.PurpleRoadmapLight,
        studyIcon = PrimitiveColors.White,
        assignmentContainer = PrimitiveColors.OrangeRoadmapLight,
        assignmentIcon = PrimitiveColors.White,
        quizContainer = PrimitiveColors.Green500,
        quizIcon = PrimitiveColors.White,
        examContainer = PrimitiveColors.Red500,
        examIcon = PrimitiveColors.White,
        projectContainer = PrimitiveColors.Blue500,
        projectIcon = PrimitiveColors.White,
    ),
)

// ──────────────────────────────────────────────────────────────
// Dark Theme
// ──────────────────────────────────────────────────────────────
private val DarkBackground = PrimitiveColors.BlackDark
private val DarkSurface = PrimitiveColors.BlackDark
private val DarkCard = PrimitiveColors.BlackDark

val DarkColorScheme = AppColorScheme(
    brand = BrandColors(
        primary = PrimitiveColors.PurpleDark,
        primaryVariant = PrimitiveColors.PurpleDark,
        primaryContainer = PrimitiveColors.PurpleDark,
        onPrimary = PrimitiveColors.White,
        onPrimaryContainer = PrimitiveColors.GrayLightDark,
        indicatorUnselected = PrimitiveColors.GrayBorderDark,
        roadmapPurple = PrimitiveColors.PurpleRoadmapDark,
        roadmapGreen = PrimitiveColors.GreenDark,
        roadmapOrange = PrimitiveColors.OrangeRoadmapDark,
        roadmapBlue = PrimitiveColors.BlueDark,
        roadmapTimeline = PrimitiveColors.GrayBorderDark,
    ),
    text = TextColors(
        primary = PrimitiveColors.GrayLightDark,
        secondary = PrimitiveColors.GraySecondaryDark,
        tertiary = PrimitiveColors.GrayTertiaryDark,
        hint = PrimitiveColors.GrayTertiaryDark,
        disabled = PrimitiveColors.GrayBorderDark,
        inverse = PrimitiveColors.BlackDark,
        dialogLabel = PrimitiveColors.GraySecondaryDark,
    ),
    surface = SurfaceColors(
        background = DarkBackground,
        surface = DarkSurface,
        surfaceLow = DarkCard,
        surfaceVariant = DarkCard,
        surfaceContainer = DarkCard,
        surfaceHigh = DarkSurface,
        surfaceHighest = DarkSurface,
    ),
    border = BorderColors(
        primary = PrimitiveColors.GrayBorderDark,
        secondary = PrimitiveColors.GrayBorderDark,
        focused = PrimitiveColors.PurpleDark,
        error = PrimitiveColors.RedDark,
        success = PrimitiveColors.GreenDark,
        disabled = PrimitiveColors.GrayBorderDark,
    ),
    state = StateColors(
        success = PrimitiveColors.GreenDark,
        successContainer = PrimitiveColors.GreenDark,
        warning = PrimitiveColors.OrangeRoadmapDark,
        warningContainer = PrimitiveColors.OrangeRoadmapDark,
        error = PrimitiveColors.RedDark,
        errorContainer = PrimitiveColors.ErrorDark,
        info = PrimitiveColors.BlueDark,
        infoContainer = PrimitiveColors.BlueDark,
    ),
    button = ButtonColors(
        primaryBackground = PrimitiveColors.PurpleDark,
        primaryContent = PrimitiveColors.White,
        secondaryBackground = DarkSurface,
        secondaryContent = PrimitiveColors.White,
        secondaryBorder = PrimitiveColors.GrayBorderDark,
        disabledBackground = PrimitiveColors.GrayBorderDark,
        disabledContent = PrimitiveColors.GraySecondaryDark,
    ),
    input = InputColors(
        background = DarkCard,
        text = PrimitiveColors.White,
        placeholder = PrimitiveColors.GrayTertiaryDark,
        border = PrimitiveColors.GrayBorderDark,
        focusedBorder = PrimitiveColors.PurpleDark,
        errorBorder = PrimitiveColors.RedDark,
        icon = PrimitiveColors.GraySecondaryDark,
    ),
    chip = ChipColors(
        selectedBackground = PrimitiveColors.PurpleDark,
        selectedContent = PrimitiveColors.White,
        unselectedBackground = DarkSurface,
        unselectedContent = PrimitiveColors.GrayLightDark,
        unselectedBorder = PrimitiveColors.GrayBorderDark,
    ),
    navigation = NavigationColors(
        background = DarkSurface,
        activeIcon = PrimitiveColors.PurpleDark,
        activeLabel = PrimitiveColors.PurpleDark,
        inactiveIcon = PrimitiveColors.GraySecondaryDark,
        inactiveLabel = PrimitiveColors.GraySecondaryDark,
        indicator = PrimitiveColors.PurpleDark,
    ),
    card = CardColors(
        background = DarkCard,
        border = PrimitiveColors.GrayBorderDark,
    ),
    events = EventColors(
        studyContainer = PrimitiveColors.PurpleRoadmapDark,
        studyIcon = PrimitiveColors.White,
        assignmentContainer = PrimitiveColors.OrangeRoadmapDark,
        assignmentIcon = PrimitiveColors.White,
        quizContainer = PrimitiveColors.GreenDark,
        quizIcon = PrimitiveColors.White,
        examContainer = PrimitiveColors.RedDark,
        examIcon = PrimitiveColors.White,
        projectContainer = PrimitiveColors.BlueDark,
        projectIcon = PrimitiveColors.White,
    ),
)
