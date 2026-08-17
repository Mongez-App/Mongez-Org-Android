package com.iti.mongez.org.presentation.profile

sealed class ProfileIntent {
    object FetchProfileData : ProfileIntent()
    data class ToggleDarkMode(val enabled: Boolean) : ProfileIntent()
    data class SelectLanguage(val language: String) : ProfileIntent()
    data class ToggleAvatarDialog(val show: Boolean) : ProfileIntent()
    data class SelectAvatar(val url: String) : ProfileIntent()
    object RemoveAvatar : ProfileIntent()
    data class ToggleLogoutDialog(val show: Boolean) : ProfileIntent()
    object ConfirmLogout : ProfileIntent()
}

sealed class ProfileEffect {
    object NavigateToLogin : ProfileEffect()
}

