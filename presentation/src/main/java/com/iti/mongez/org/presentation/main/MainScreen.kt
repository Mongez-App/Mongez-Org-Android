package com.iti.mongez.org.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iti.mongez.org.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.org.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.org.designsystem.theme.Theme

@Composable
fun MainScreen(
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToTeamDetails: (String) -> Unit
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
            innerPadding = innerPadding
        )
    }
}

@Composable
private fun MainScreenContent(
    tab: MainTab,
    innerPadding: PaddingValues
) {
    // Render the active tab content
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "${tab.name} Screen Placeholder")
    }
}
