package com.iti.mongez.org.presentation.team_details.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.org.presentation.team_details.contract.TeamDetailsIntent
import com.iti.mongez.org.presentation.team_details.viewmodel.TeamDetailsViewModel


@Composable
fun TeamDetailsScreen(
    teamId: String,
    teamName: String,
    onNavigateBack: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    viewModel: TeamDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(teamId) {
        viewModel.handleIntent(TeamDetailsIntent.LoadTeam(teamId, teamName))
    }

    TeamDetailsScreenContent(
        state = state,
        innerPadding = PaddingValues(0.dp),
        onIntent = viewModel::handleIntent,
        onNavigateBack = onNavigateBack,
        onNavigateToCourseDetails = onNavigateToCourseDetails
    )
}
