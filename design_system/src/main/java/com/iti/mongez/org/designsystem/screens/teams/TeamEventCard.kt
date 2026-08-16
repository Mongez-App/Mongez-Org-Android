package com.iti.mongez.org.designsystem.screens.teams

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme

enum class TeamEventColor(val mainColor: Color) {
    Green(Color(0xFF10B981)),
    Orange(Color(0xFFF59E0B)),
    Purple(Color(0xFF8B5CF6)),
    Cyan(Color(0xFF06B6D4)),
    Blue(Color(0xFF3B82F6)),
    LightBlue(Color(0xFF60A5FA)),
    Red(Color(0xFFEF4444))
}

@Composable
fun TeamEventCard(
    courseName: String,
    eventTitle: String,
    statusText: String,
    eventColor: TeamEventColor,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val backgroundColor = eventColor.mainColor.copy(alpha = 0.05f)
    val borderColor = eventColor.mainColor.copy(alpha = 0.2f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(Theme.radius.lg),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(1.dp, borderColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.spacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = courseName,
                    style = Theme.typography.body.small.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    ),
                    color = eventColor.mainColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = eventTitle,
                    style = Theme.typography.label.large.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = Theme.colorScheme.text.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = statusText,
                    style = Theme.typography.body.medium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = eventColor.mainColor
                )
            }

            Icon(
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = null,
                tint = eventColor.mainColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeamEventCardPreview() {
    MongezTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TeamEventCard(
                    courseName = "Operating Systems",
                    eventTitle = "Midterm",
                    statusText = "Tomorrow",
                    eventColor = TeamEventColor.Green,
                    modifier = Modifier.weight(1f)
                )
                TeamEventCard(
                    courseName = "Operating Systems",
                    eventTitle = "Project",
                    statusText = "1 days left",
                    eventColor = TeamEventColor.Orange,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TeamEventCard(
                    courseName = "Mobile Using Obje...",
                    eventTitle = "Quiz",
                    statusText = "2 days left",
                    eventColor = TeamEventColor.Purple,
                    modifier = Modifier.weight(1f)
                )
                TeamEventCard(
                    courseName = "Data Structures &...",
                    eventTitle = "Midterm",
                    statusText = "3 days left",
                    eventColor = TeamEventColor.Cyan,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
