package com.iti.mongez.org.presentation.profile

data class ProfileUiState(
    val organizationName: String = "Organization Name",
    val email: String = "myorganization@gmail.com",
    val avatarUrl: String? = null,
    val isDarkMode: Boolean = false,
    val currentLanguage: String = "EN",
    val showAvatarDialog: Boolean = false,
    val showLogoutDialog: Boolean = false
)