package com.iti.mongez.org.presentation.auth.register.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.org.designsystem.R
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.textfield.AppPasswordTextField
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.auth.components.StepIndicator
import com.iti.mongez.org.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterUiState

@Composable
fun SignUp1Screen(
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
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = { onIntent(RegisterIntent.OnBackToLogin) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Login",
                    tint = Theme.colorScheme.text.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.md))
        
        StepIndicator(
            totalSteps = 5,
            currentStep = 1,
            modifier = Modifier.padding(horizontal = Theme.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.register_illustration),
                contentDescription = "Register Illustration",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Text(
            text = stringResource(R.string.register_create_account),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        AppTextField(
            value = uiState.orgName,
            onValueChange = { onIntent(RegisterIntent.OnOrgNameChanged(it)) },
            label = stringResource(R.string.register_org_name_label),
            placeholder = stringResource(R.string.register_org_name_hint),
            isError = uiState.orgNameError != null,
            errorMessage = uiState.orgNameError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = uiState.email,
            onValueChange = { onIntent(RegisterIntent.OnEmailChanged(it)) },
            label = stringResource(R.string.login_email_label),
            placeholder = stringResource(R.string.login_email_hint),
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppPasswordTextField(
            value = uiState.password,
            onValueChange = { onIntent(RegisterIntent.OnPasswordChanged(it)) },
            label = stringResource(R.string.login_password_label),
            placeholder = stringResource(R.string.login_password_hint),
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_password_lock),
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppPasswordTextField(
            value = uiState.confirmPassword,
            onValueChange = { onIntent(RegisterIntent.OnConfirmPasswordChanged(it)) },
            label = stringResource(R.string.register_confirm_password_label),
            placeholder = stringResource(R.string.register_confirm_password_hint),
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_password_lock),
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        AppButton(
            text = stringResource(R.string.register_next),
            onClick = { onIntent(RegisterIntent.OnStep1Next) },
            isLoading = uiState.isLoading,
            fullWidth = true
        )
        
        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUp1ScreenPreview() {
    MongezTheme {
        SignUp1Screen(
            uiState = RegisterUiState(),
            onIntent = {}
        )
    }
}
