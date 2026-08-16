package com.iti.mongez.org.presentation.teams.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.components.common.AppEmptyState
import com.iti.mongez.org.designsystem.screens.teams.TeamEventCard
import com.iti.mongez.org.designsystem.screens.teams.TeamEventColor
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.domain.teams.model.TeamEvent
import com.iti.mongez.org.presentation.R

@Composable
fun TeamEventsList(
    events: List<TeamEvent>,
    onAddEventClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (events.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            AppEmptyState(
                title = "No Events Yet",
                description = "You don’t have scheduled events for this team",
                actionText = "Let’s Add an Event",
                onAction = onAddEventClick,
                illustration = {
                    // TODO: Add proper illustration if available
                }
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(Theme.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            items(events) { event ->
                // Mock mapping to UI fields for now
                val colors = TeamEventColor.values()
                val color = colors[event.hashCode().coerceAtLeast(0) % colors.size]
                
                TeamEventCard(
                    courseName = "Course Name", // event.courseName if it existed
                    eventTitle = event.title,
                    statusText = event.date, // event.status if it existed
                    eventColor = color
                )
            }
        }
    }
}
