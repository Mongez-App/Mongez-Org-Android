package com.iti.mongez.org.presentation.teams.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
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
            contentAlignment = Alignment.Center
        ) {
            AppEmptyState(
                title = stringResource(R.string.no_events_yet_team),
                description = stringResource(R.string.no_events_team_desc),
                actionText = stringResource(R.string.lets_add_event),
                onAction = onAddEventClick,
                illustration = {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .background(Theme.colorScheme.brand.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_empty_events),
                            contentDescription = null,
                            modifier = Modifier.size(110.dp)
                        )
                    }
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
                val colors = TeamEventColor.entries
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
