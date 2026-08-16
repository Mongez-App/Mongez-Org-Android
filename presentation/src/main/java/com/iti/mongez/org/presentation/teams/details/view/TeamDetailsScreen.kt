package com.iti.mongez.org.presentation.teams.details.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.org.presentation.teams.details.contract.TeamDetailsIntent
import com.iti.mongez.org.presentation.teams.details.viewmodel.TeamDetailsViewModel

@Composable
fun TeamDetailsScreen(
    teamId: String,
    onNavigateBack: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    viewModel: TeamDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.handleIntent(TeamDetailsIntent.LoadTeam(teamId))
    }

    TeamDetailsScreenContent(
        state = state,
        innerPadding = PaddingValues(0.dp),
        onIntent = viewModel::handleIntent,
        onNavigateBack = onNavigateBack,
        onNavigateToCourseDetails = onNavigateToCourseDetails
    )
}
