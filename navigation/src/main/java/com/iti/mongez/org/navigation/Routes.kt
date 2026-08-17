package com.iti.mongez.org.navigation

/**
 * Type-safe sealed interface defining all navigation routes
 * in the MongezOrg application.
 */
sealed interface AppRoute {
    data object Onboarding : AppRoute
    data object Login : AppRoute
    data object SignUp1 : AppRoute
    data object SignUp2 : AppRoute
    data object SignUp3 : AppRoute
    data object SignUp4 : AppRoute
    data object LocationPicker : AppRoute
    data object UnderReview : AppRoute
    data object Verified : AppRoute
    data object PlanSelection : AppRoute
    data object Main : AppRoute
    data object Courses : AppRoute
    data class TeamDetails(val teamId: String, val teamName: String) : AppRoute
    data class CourseDetails(val courseId: String) : AppRoute
    data object Events : AppRoute
    data object Tasks : AppRoute
    data object MagicBox : AppRoute
}
