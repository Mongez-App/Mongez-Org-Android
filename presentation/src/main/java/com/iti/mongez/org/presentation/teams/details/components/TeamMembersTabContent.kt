package com.iti.mongez.org.presentation.teams.details.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.components.common.AppEmptyState
import com.iti.mongez.org.designsystem.screens.teams.TeamMemberCard
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.domain.teams.model.Member

@Composable
fun TeamMembersTabContent(
    members: List<Member>,
    pendingMembers: List<Member>,
    onAcceptMember: (String) -> Unit,
    onDeclineMember: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarColors = listOf(
        Color(0xFF8B5CF6), // Purple
        Color(0xFFF59E0B), // Orange
        Color(0xFF10B981), // Green
        Color(0xFF3B82F6), // Blue
        Color(0xFFEF4444)  // Red
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = Theme.spacing.xxl),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
    ) {
        // Pending Members Section
        item {
            Text(
                text = "Pending Members",
                style = Theme.typography.title.medium,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = Theme.spacing.md)
            )
        }

        if (pendingMembers.isEmpty()) {
            item {
                Text(
                    text = "No Pending Members for the Current time",
                    style = Theme.typography.body.medium,
                    color = Theme.colorScheme.text.tertiary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Theme.spacing.xxl),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(pendingMembers, key = { it.id }) { member ->
                val color = avatarColors[member.id.hashCode().coerceAtLeast(0) % avatarColors.size]
                TeamMemberCard(
                    name = member.name,
                    avatarColor = color,
                    isPending = true,
                    onAccept = { onAcceptMember(member.id) },
                    onDecline = { onDeclineMember(member.id) }
                )
            }
        }

        // Team Members Section
        item {
            Spacer(modifier = Modifier.height(Theme.spacing.lg))
            Text(
                text = "Team Members",
                style = Theme.typography.title.medium,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold
            )
        }

        if (members.isEmpty()) {
            item {
                AppEmptyState(
                    title = "",
                    description = "No team members added yet",
                    illustration = {
                        Icon(
                            imageVector = Icons.Rounded.Group,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            tint = Color(0xFF6366F1).copy(alpha = 0.8f) // Matching screenshot color roughly
                        )
                    }
                )
            }
        } else {
            items(members, key = { it.id }) { member ->
                val color = avatarColors[member.id.hashCode().coerceAtLeast(0) % avatarColors.size]
                TeamMemberCard(
                    name = member.name,
                    avatarColor = color
                )
            }
        }
    }
}
