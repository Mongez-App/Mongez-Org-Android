package com.iti.mongez.org.presentation.auth.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.iti.mongez.org.presentation.auth.login.contract.LoginIntent
import com.iti.mongez.org.presentation.auth.login.uiState.LoginUiState

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_illustration),
                contentDescription = "Login Illustration",
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Text(
            text = stringResource(R.string.login_welcome_back),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        AppTextField(
            value = uiState.email,
            onValueChange = { onIntent(LoginIntent.OnEmailChanged(it)) },
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
            onValueChange = { onIntent(LoginIntent.OnPasswordChanged(it)) },
            label = stringResource(R.string.login_password_label),
            placeholder = stringResource(R.string.login_password_hint),
            leadingIcon = ImageVector.vectorResource(id = R.drawable.ic_password_lock),
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Theme.spacing.sm))
        
        Text(
            text = stringResource(R.string.login_forgot_password),
            color = Theme.colorScheme.brand.primary,
            style = Theme.typography.body.large,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onIntent(LoginIntent.OnForgotPasswordClicked) }
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        AppButton(
            text = stringResource(R.string.login_sign_in),
            onClick = { onIntent(LoginIntent.OnLoginClicked) },
            isLoading = uiState.isLoading,
            fullWidth = true
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        Spacer(modifier = Modifier.height(Theme.spacing.xl))



        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.login_no_account),
                color = Theme.colorScheme.text.secondary,
                style = Theme.typography.body.large
            )
            Text(
                text = stringResource(R.string.login_sign_up),
                color = Theme.colorScheme.brand.primary,
                style = Theme.typography.body.large,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onIntent(LoginIntent.OnSignUpClicked) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MongezTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onIntent = {}
        )
    }
}
