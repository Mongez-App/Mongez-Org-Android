package com.iti.mongez.org.presentation.auth.register.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.org.designsystem.R
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.auth.components.DynamicListField
import com.iti.mongez.org.presentation.auth.components.StepIndicator
import com.iti.mongez.org.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterUiState

@Composable
fun SignUp3Screen(
    uiState: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface.background)
            .safeDrawingPadding()
            .verticalScroll(scrollState)
            .padding(Theme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.lg))
        
        StepIndicator(
            totalSteps = 5,
            currentStep = 3,
            modifier = Modifier.padding(horizontal = Theme.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))
        
        Text(
            text = stringResource(R.string.register_services_audience),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        DynamicListField(
            label = stringResource(R.string.register_target_audience_label),
            values = uiState.targetAudience,
            onValuesChange = { onIntent(RegisterIntent.OnTargetAudienceChanged(it)) },
            placeholder = stringResource(R.string.register_target_audience_hint),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        DynamicListField(
            label = stringResource(R.string.register_services_label),
            values = uiState.services,
            onValuesChange = { onIntent(RegisterIntent.OnServicesChanged(it)) },
            placeholder = stringResource(R.string.register_services_hint),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        AppTextField(
            value = uiState.membersCount,
            onValueChange = { onIntent(RegisterIntent.OnMembersCountChanged(it)) },
            label = stringResource(R.string.register_members_count_label),
            placeholder = stringResource(R.string.register_members_count_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.membersCountError != null,
            errorMessage = uiState.membersCountError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Row(modifier = Modifier.fillMaxWidth()) {
            AppButton(
                text = stringResource(R.string.register_back),
                onClick = { onIntent(RegisterIntent.OnStep3Back) },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(Theme.spacing.md))
            
            AppButton(
                text = stringResource(R.string.register_next),
                onClick = { onIntent(RegisterIntent.OnStep3Next) },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUp3ScreenPreview() {
    MongezTheme {
        SignUp3Screen(
            uiState = RegisterUiState(),
            onIntent = {}
        )
    }
}
