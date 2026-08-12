package com.iti.mongez.org.presentation.auth.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme

@Composable
fun StepIndicator(
    totalSteps: Int,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (step in 1..totalSteps) {
            val isCompleted = step < currentStep
            val isActive = step == currentStep
            val color = when {
                isActive || isCompleted -> Theme.colorScheme.brand.primary
                else -> Theme.colorScheme.border.primary
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = step.toString(),
                    color = if (isActive || isCompleted) Color.White else Theme.colorScheme.text.secondary,
                    style = Theme.typography.label.large,
                    textAlign = TextAlign.Center
                )
            }

            if (step < totalSteps) {
                val lineColor = if (isCompleted) Theme.colorScheme.brand.primary else Theme.colorScheme.border.primary
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .padding(horizontal = Theme.spacing.xs)
                ) {
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StepIndicatorPreview() {
    MongezTheme {
        StepIndicator(
            totalSteps = 5,
            currentStep = 3,
            modifier = Modifier.padding(16.dp)
        )
    }
}
