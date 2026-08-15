package com.iti.mongez.org.presentation.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onDarkModeChanged(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }

    fun onLanguageSelected(language: String) {
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun onAvatarClicked(show: Boolean) {
        _uiState.update { it.copy(showAvatarDialog = show) }
    }

    fun onAvatarSelected(url: String) {
        _uiState.update { it.copy(avatarUrl = url, showAvatarDialog = false) }
    }

    fun onRemoveAvatar() {
        _uiState.update { it.copy(avatarUrl = null, showAvatarDialog = false) }
    }

    fun onLogoutClicked(show: Boolean) {
        _uiState.update { it.copy(showLogoutDialog = show) }
    }
}