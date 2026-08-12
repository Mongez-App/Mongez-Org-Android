package com.iti.mongez.org.navigation

/**
 * Type-safe sealed interface defining all navigation routes
 * in the MongezOrg application.
 */
sealed interface AppRoute {
    data object Onboarding : AppRoute
    data object Login : AppRoute
    data object Register : AppRoute
    data object Dashboard : AppRoute
    data object Teams : AppRoute
    data class TeamDetails(val teamId: String) : AppRoute
    data class CourseDetails(val courseId: String) : AppRoute
    data object Members : AppRoute
    data object Events : AppRoute
    data object Tasks : AppRoute
    data object Profile : AppRoute
    data object MagicBox : AppRoute
}
