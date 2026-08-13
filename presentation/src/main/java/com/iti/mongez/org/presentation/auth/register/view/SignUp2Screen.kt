package com.iti.mongez.org.presentation.auth.register.view

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iti.mongez.org.designsystem.R
import com.iti.mongez.org.designsystem.components.button.AppButton
import com.iti.mongez.org.designsystem.components.textfield.AppTextField
import com.iti.mongez.org.designsystem.theme.MongezTheme
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.org.presentation.auth.components.StepIndicator
import com.iti.mongez.org.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp2Screen(
    uiState: RegisterUiState,
    onIntent: (RegisterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val industries = listOf(
        stringResource(R.string.register_industry_tech),
        stringResource(R.string.register_industry_edu),
        stringResource(R.string.register_industry_health),
        stringResource(R.string.register_industry_finance),
        stringResource(R.string.register_industry_retail),
        stringResource(R.string.register_industry_manufacturing),
        stringResource(R.string.register_industry_nonprofit),
        stringResource(R.string.register_industry_gov),
        stringResource(R.string.register_industry_media),
        stringResource(R.string.register_industry_other)
    )
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            if (uri != null) {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    // Ignored
                }
                onIntent(RegisterIntent.OnLogoSelected(uri.toString()))
            }
        }
    )

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
            currentStep = 2,
            modifier = Modifier.padding(horizontal = Theme.spacing.lg)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))
        
        Text(
            text = stringResource(R.string.register_basic_info),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Theme.colorScheme.brand.primary.copy(alpha = 0.1f))
                .clickable { 
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.logoUri != null) {
                AsyncImage(
                    model = uiState.logoUri,
                    contentDescription = "Selected Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_image_placeholder),
                    contentDescription = "Add Photo",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(Theme.spacing.sm))
        Text(text = stringResource(R.string.register_add_logo), color = Theme.colorScheme.text.secondary)

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            AppTextField(
                value = uiState.industryField,
                onValueChange = {},
                label = stringResource(R.string.register_industry_label),
                placeholder = stringResource(R.string.register_industry_hint),
                readOnly = true,
                isError = uiState.industryFieldError != null,
                errorMessage = uiState.industryFieldError,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                    .fillMaxWidth()
            )
            
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Theme.colorScheme.brand.onPrimary
            ) {
                industries.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onIntent(RegisterIntent.OnIndustryFieldChanged(selectionOption))
                            expanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.md))

        AppTextField(
            value = uiState.description,
            onValueChange = { onIntent(RegisterIntent.OnDescriptionChanged(it)) },
            label = stringResource(R.string.register_desc_label),
            placeholder = stringResource(R.string.register_desc_hint),
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xl))

        Row(modifier = Modifier.fillMaxWidth()) {
            AppButton(
                text = stringResource(R.string.register_back),
                onClick = { onIntent(RegisterIntent.OnStep2Back) },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(Theme.spacing.md))
            
            AppButton(
                text = stringResource(R.string.register_next),
                onClick = { onIntent(RegisterIntent.OnStep2Next) },
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(Theme.spacing.xl))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUp2ScreenPreview() {
    MongezTheme {
        SignUp2Screen(
            uiState = RegisterUiState(),
            onIntent = {}
        )
    }
}
