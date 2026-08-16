package com.iti.mongez.org.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.org.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.org.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.R
import com.iti.mongez.org.presentation.profile.ProfileScreen
import com.iti.mongez.org.presentation.profile.ProfileViewModel
import com.iti.mongez.org.presentation.teams.TeamsScreen
import com.iti.mongez.org.presentation.teams.TeamsViewModel

@Composable
fun MainScreen(
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToTeamDetails: (String) -> Unit,
    onNavigateToLogin: () -> Unit = {}
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedTab = MainTab.fromIndex(selectedTabIndex)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colorScheme.surface.background,
        bottomBar = {
            AppNavigationBar(
                items = MainTab.entries.map { tab ->
                    AppNavigationBarItem(
                        label = "Tab", // Simplified label, normally use stringResource(tab.labelResId)
                        selectedIcon = tab.selectedIcon,
                        unselectedIcon = tab.unselectedIcon
                    )
                },
                selectedIndex = selectedTabIndex,
                onItemSelected = { selectedTabIndex = it }
            )
        }
    ) { innerPadding ->
        MainScreenContent(
            tab = selectedTab,
            innerPadding = innerPadding,
            onNavigateToCourseDetails = onNavigateToCourseDetails,
            onNavigateToTeamDetails = onNavigateToTeamDetails,
            onNavigateToLogin = onNavigateToLogin
        )
    }
}

@Composable
private fun MainScreenContent(
    tab: MainTab,
    innerPadding: PaddingValues,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToTeamDetails: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center
    ) {
        when (tab) {
            MainTab.Dashboard -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Theme.spacing.xl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dashboard_under_construction),
                        contentDescription = stringResource(id = R.string.dashboard_coming_soon),
                        modifier = Modifier.size(220.dp)
                    )
                    Spacer(modifier = Modifier.height(Theme.spacing.lg))
                    Text(
                        text = stringResource(id = R.string.dashboard_coming_soon),
                        style = Theme.typography.title.large,
                        color = Theme.colorScheme.text.secondary
                    )
                }
            }
            MainTab.Teams -> {
                val teamsViewModel: TeamsViewModel = hiltViewModel()
                TeamsScreen(
                    viewModel = teamsViewModel,
                    onNavigateToCourseDetails = onNavigateToCourseDetails
                )
            }
            MainTab.Profile -> {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }
    }
}
