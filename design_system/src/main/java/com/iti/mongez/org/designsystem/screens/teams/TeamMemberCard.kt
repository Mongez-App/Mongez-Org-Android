package com.iti.mongez.org.designsystem.screens.teams

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.designsystem.R as DesignR

@Composable
fun TeamMemberCard(
    name: String,
    avatarColor: Color,
    modifier: Modifier = Modifier,
    isPending: Boolean = false,
    onAccept: () -> Unit = {},
    onDecline: () -> Unit = {}
) {
    val initials = name.split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.take(1).uppercase() }

    Column(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(Theme.radius.lg),
            colors = CardDefaults.cardColors(
                containerColor = Theme.colorScheme.surface.background
            ),
            border = BorderStroke(1.dp, Theme.colorScheme.card.border)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Theme.spacing.lg),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with initials
                Box(
                    modifier = Modifier
                        .size(48.dp) // Slightly smaller as per screenshot
                        .clip(CircleShape)
                        .background(avatarColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials,
                        style = Theme.typography.title.medium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(Theme.spacing.lg))

                Text(
                    text = name,
                    style = Theme.typography.label.large.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    color = Theme.colorScheme.text.primary
                )
            }
        }

        if (isPending) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, end = Theme.spacing.lg),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = DesignR.string.decline),
                    style = Theme.typography.body.small.copy(fontWeight = FontWeight.Medium),
                    color = Theme.colorScheme.state.error,
                    modifier = Modifier.clickable { onDecline() }
                )

                Text(
                    text = " | ",
                    color = Theme.colorScheme.text.tertiary,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Text(
                    text = stringResource(id = DesignR.string.accept),
                    style = Theme.typography.body.small.copy(fontWeight = FontWeight.Medium),
                    color = Theme.colorScheme.state.success,
                    modifier = Modifier.clickable { onAccept() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamMemberCardPreview() {
    MongezTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TeamMemberCard(
                name = "Member Name",
                avatarColor = Color(0xFF8B5CF6),
                isPending = true
            )
            TeamMemberCard(
                name = "Member Name",
                avatarColor = Color(0xFFF59E0B)
            )
        }
    }
}
