package com.iti.mongez.org.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iti.mongez.org.designsystem.theme.Theme
import com.iti.mongez.presentation.profile.components.AvatarPickerDialog
import com.iti.mongez.presentation.profile.components.SettingItem

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToLogin: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchProfileData() // Replace with the actual name of your fetch function
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Theme.colorScheme.surface.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Theme.spacing.xxl))

            // Profile Avatar / Initials Container
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .border(2.dp, Theme.colorScheme.brand.primary, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.brand.primaryContainer)
                    .clickable { viewModel.onAvatarClicked(true) },
                contentAlignment = Alignment.Center
            ) {
                if (state.avatarUrl != null) {
                    AsyncImage(
                        model = state.avatarUrl,
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "ON",
                        style = Theme.typography.title.large,
                        color = Theme.colorScheme.brand.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(Theme.spacing.md))

            Text(
                text = state.organizationName,
                style = Theme.typography.title.large,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Theme.spacing.xs))

            Text(
                text = state.email,
                style = Theme.typography.body.medium,
                color = Theme.colorScheme.text.secondary
            )

            Spacer(modifier = Modifier.height(Theme.spacing.xxl))

            // Settings List Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
            ) {
                // Dark Mode Setting
                SettingItem(
                    icon = Icons.Rounded.DarkMode,
                    iconContainerColor = Theme.colorScheme.brand.primaryContainer,
                    iconTint = Theme.colorScheme.brand.primary,
                    title = "Dark Mode",
                    action = {
                        Switch(
                            checked = state.isDarkMode,
                            onCheckedChange = { viewModel.onDarkModeChanged(it) }
                        )
                    }
                )

                HorizontalDivider(color = Theme.colorScheme.border.secondary, thickness = 1.dp)

                // Language Setting
                SettingItem(
                    icon = Icons.Rounded.Translate,
                    iconContainerColor = Theme.colorScheme.brand.primaryContainer,
                    iconTint = Theme.colorScheme.brand.primary,
                    title = "Language",
                    action = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.xs)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(Theme.radius.sm),
                                color = Theme.colorScheme.surface.surfaceVariant,
                                border = BorderStroke(1.dp, Theme.colorScheme.border.secondary)
                            ) {
                                Text(
                                    text = state.currentLanguage,
                                    modifier = Modifier.padding(horizontal = Theme.spacing.sm, vertical = Theme.spacing.xxs),
                                    style = Theme.typography.body.medium,
                                    fontWeight = FontWeight.Medium,
                                    color = Theme.colorScheme.text.primary
                                )
                            }
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Theme.colorScheme.text.secondary
                            )
                        }
                    },
                    onClick = {
                        val nextLang = if (state.currentLanguage == "EN") "AR" else "EN"
                        viewModel.onLanguageSelected(nextLang)
                    }
                )

                HorizontalDivider(color = Theme.colorScheme.border.secondary, thickness = 1.dp)

                // Logout Setting
                SettingItem(
                    icon = Icons.AutoMirrored.Rounded.Logout,
                    iconContainerColor = Theme.colorScheme.state.errorContainer,
                    iconTint = Theme.colorScheme.state.error,
                    title = "Logout",
                    titleColor = Theme.colorScheme.state.error,
                    action = {
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Theme.colorScheme.text.secondary
                        )
                    },
                    onClick = {
                        viewModel.onLogoutClicked(true)
                    }
                )

                HorizontalDivider(color = Theme.colorScheme.border.secondary, thickness = 1.dp)
            }
        }

        // Avatar Picker Dialog
        if (state.showAvatarDialog) {
            AvatarPickerDialog(
                selectedAvatarUrl = state.avatarUrl,
                onAvatarSelected = { viewModel.onAvatarSelected(it) },
                onRemoveAvatar = { viewModel.onRemoveAvatar() },
                onDismiss = { viewModel.onAvatarClicked(false) }
            )
        }

        // Logout Confirmation Dialog
        if (state.showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.onLogoutClicked(false) },
                title = { Text(text = "Logout") },
                text = { Text(text = "Are you sure you want to logout?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.onLogoutClicked(false)
                            onNavigateToLogin()
                        }
                    ) {
                        Text(text = "Logout", color = Theme.colorScheme.state.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onLogoutClicked(false) }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }
}