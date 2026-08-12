package com.iti.mongez.org.presentation.auth.register.view

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.R
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.auth.components.StepIndicator
import com.iti.mongez.org.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterUiState

@Composable
fun ReviewScreen(
    uiState: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface.background)
            .safeDrawingPadding()
            .padding(Theme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.lg))
        
        StepIndicator(
            totalSteps = 5,
            currentStep = 6, // 6 means all 5 are completed
            modifier = Modifier.padding(horizontal = Theme.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

        AnimatedContent(
            targetState = uiState.isAccepted,
            label = "ReviewStateTransition"
        ) { isAccepted ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.register_illustration),
                        contentDescription = "Review Illustration",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(Theme.spacing.xl))

                if (isAccepted) {
                    Text(
                        text = stringResource(R.string.review_verified),
                        style = Theme.typography.headline.medium,
                        color = Theme.colorScheme.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Theme.spacing.md))
                    
                    Text(
                        text = stringResource(R.string.review_verified_desc),
                        style = Theme.typography.body.large,
                        color = Theme.colorScheme.text.secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = Theme.spacing.lg)
                    )
                    
                    Spacer(modifier = Modifier.height(Theme.spacing.xxl))
                    
                    AppButton(
                        text = stringResource(R.string.review_go_to_dashboard),
                        onClick = { onIntent(RegisterIntent.OnNavigateToHome) },
                        fullWidth = true
                    )
                } else {
                    Text(
                        text = stringResource(R.string.review_under_review),
                        style = Theme.typography.headline.medium,
                        color = Theme.colorScheme.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(Theme.spacing.md))
                    
                    Text(
                        text = stringResource(R.string.review_under_review_desc),
                        style = Theme.typography.body.large,
                        color = Theme.colorScheme.text.secondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = Theme.spacing.lg)
                    )
                    
                    Spacer(modifier = Modifier.height(Theme.spacing.xxl))
                    
                    AppButton(
                        text = stringResource(R.string.review_back_to_login),
                        onClick = { onIntent(RegisterIntent.OnBackToLogin) },
                        fullWidth = true
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenUnderReviewPreview() {
    MongezTheme {
        ReviewScreen(
            uiState = RegisterUiState(isReviewComplete = true, isAccepted = false),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ReviewScreenAcceptedPreview() {
    MongezTheme {
        ReviewScreen(
            uiState = RegisterUiState(isReviewComplete = true, isAccepted = true),
            onIntent = {}
        )
    }
}
