package com.iti.mongez.org.presentation.courses.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.org.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.org.presentation.courses.contract.CoursesEffect
import com.iti.mongez.org.presentation.courses.contract.CoursesIntent
import com.iti.mongez.org.presentation.courses.viewmodel.CoursesViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CoursesScreen(
    teamId: String = "",
    teamName: String = "",
    innerPadding: PaddingValues,
    onCourseClick: (String) -> Unit,
    onNavigateBack: () -> Unit = {},
    viewModel: CoursesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var topSnackbarMessage by remember { mutableStateOf<String?>(null) }
    var topSnackbarType by remember { mutableStateOf(AppSnackbarType.Info) }

    LaunchedEffect(teamId, teamName) {
        // Automatically fetch latest list whenever returning to CoursesScreen
        viewModel.handleIntent(CoursesIntent.LoadCourses(teamId, teamName))

        viewModel.effect.collect { effect ->
            when (effect) {
                is CoursesEffect.ShowSnackbar -> {
                    topSnackbarMessage = effect.message
                    topSnackbarType = effect.type
                }
                is CoursesEffect.NavigateToUploadMaterial -> {}
                is CoursesEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    LaunchedEffect(topSnackbarMessage) {
        if (topSnackbarMessage != null) {
            delay(3000L.milliseconds)
            topSnackbarMessage = null
        }
    }

    CoursesScreenContent(
        state = state,
        innerPadding = innerPadding,
        topSnackbarMessage = topSnackbarMessage,
        topSnackbarType = topSnackbarType,
        onIntent = viewModel::handleIntent,
        onCourseClick = onCourseClick
    )
}