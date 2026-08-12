package com.iti.mongez.org.presentation.auth.register.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
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
fun SignUp4Screen(
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
            currentStep = 4,
            modifier = Modifier.padding(horizontal = Theme.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))
        
        Text(
            text = stringResource(R.string.register_contact_location),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        AppTextField(
            value = uiState.contactEmail,
            onValueChange = { onIntent(RegisterIntent.OnContactEmailChanged(it)) },
            label = stringResource(R.string.register_contact_email_label),
            placeholder = stringResource(R.string.register_contact_email_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = uiState.contactEmailError != null,
            errorMessage = uiState.contactEmailError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = uiState.phoneNumber,
            onValueChange = { onIntent(RegisterIntent.OnPhoneNumberChanged(it)) },
            label = stringResource(R.string.register_phone_label),
            placeholder = stringResource(R.string.register_phone_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = uiState.phoneNumberError != null,
            errorMessage = uiState.phoneNumberError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = uiState.websiteUrl,
            onValueChange = { onIntent(RegisterIntent.OnWebsiteUrlChanged(it)) },
            label = stringResource(R.string.register_website_label),
            placeholder = stringResource(R.string.register_website_hint),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = uiState.address,
            onValueChange = { onIntent(RegisterIntent.OnAddressChanged(it)) },
            label = stringResource(R.string.register_address_label),
            placeholder = stringResource(R.string.register_address_hint),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Theme.spacing.md))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(Theme.radius.md))
                .clickable { onIntent(RegisterIntent.OnNavigateToLocationPicker) }
        ) {
            if (uiState.latitude != 0.0) {
                val latLng = LatLng(uiState.latitude, uiState.longitude)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(latLng, 14f)
                }
                
                LaunchedEffect(latLng) {
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 14f)
                }

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        scrollGesturesEnabled = false,
                        zoomGesturesEnabled = false,
                        rotationGesturesEnabled = false,
                        tiltGesturesEnabled = false,
                        myLocationButtonEnabled = false
                    )
                ) {
                    Marker(
                        state = MarkerState(position = latLng)
                    )
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.colorScheme.brand.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.register_select_location),
                        color = Theme.colorScheme.brand.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = uiState.registrationNumber,
            onValueChange = { onIntent(RegisterIntent.OnRegistrationNumberChanged(it)) },
            label = stringResource(R.string.register_reg_number_label),
            placeholder = stringResource(R.string.register_reg_number_hint),
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        DynamicListField(
            label = stringResource(R.string.register_documents_label),
            values = if (uiState.documents.isEmpty()) listOf("") else uiState.documents,
            onValuesChange = { onIntent(RegisterIntent.OnDocumentsChanged(it)) },
            placeholder = stringResource(R.string.register_documents_hint),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Row(modifier = Modifier.fillMaxWidth()) {
            AppButton(
                text = stringResource(R.string.register_back),
                onClick = { onIntent(RegisterIntent.OnStep4Back) },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(Theme.spacing.md))
            
            AppButton(
                text = stringResource(R.string.register_submit),
                onClick = { onIntent(RegisterIntent.OnStep4Submit) },
                isLoading = uiState.isLoading,
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUp4ScreenPreview() {
    MongezTheme {
        SignUp4Screen(
            uiState = RegisterUiState(),
            onIntent = {}
        )
    }
}
