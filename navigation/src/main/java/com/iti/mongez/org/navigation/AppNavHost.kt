package com.iti.mongez.org.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay

/**
 * Root navigation host for the MongezOrg application.
 *
 * Uses Jetpack Navigation 3 with a state-driven backstack.
 * Each route renders a placeholder screen until the actual
 * feature screens are implemented.
 */
@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<AppRoute>(AppRoute.Dashboard) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() }
    ) { key ->
        when (key) {
            is AppRoute.Onboarding -> NavEntry(AppRoute.Onboarding) {
                PlaceholderScreen("Onboarding")
            }
            is AppRoute.Login -> NavEntry(AppRoute.Login) {
                PlaceholderScreen("Login")
            }
            is AppRoute.Register -> NavEntry(AppRoute.Register) {
                PlaceholderScreen("Register")
            }
            is AppRoute.Dashboard -> NavEntry(AppRoute.Dashboard) {
                PlaceholderScreen("Dashboard")
            }
            is AppRoute.Teams -> NavEntry(AppRoute.Teams) {
                PlaceholderScreen("Teams")
            }
            is AppRoute.TeamDetails -> NavEntry(key) {
                PlaceholderScreen("Team Details: ${key.teamId}")
            }
            is AppRoute.CourseDetails -> NavEntry(key) {
                PlaceholderScreen("Course Details: ${key.courseId}")
            }
            is AppRoute.Members -> NavEntry(AppRoute.Members) {
                PlaceholderScreen("Members")
            }
            is AppRoute.Events -> NavEntry(AppRoute.Events) {
                PlaceholderScreen("Events")
            }
            is AppRoute.Tasks -> NavEntry(AppRoute.Tasks) {
                PlaceholderScreen("Tasks")
            }
            is AppRoute.Profile -> NavEntry(AppRoute.Profile) {
                PlaceholderScreen("Profile")
            }
            is AppRoute.MagicBox -> NavEntry(AppRoute.MagicBox) {
                PlaceholderScreen("Magic Box")
            }
        }
    }
}

/**
 * Temporary placeholder screen used during architecture setup.
 * Will be replaced with actual feature screens.
 */
@Composable
private fun PlaceholderScreen(screenName: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = screenName)
    }
}
