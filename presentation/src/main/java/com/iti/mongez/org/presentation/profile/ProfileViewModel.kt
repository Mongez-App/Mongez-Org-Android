package com.iti.mongez.org.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.profile.usecases.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfileData()
    }


    private fun fetchProfileData() {
        viewModelScope.launch {
            getProfileUseCase().collect { result ->
                result.onSuccess { profile -> // <--- Make sure this is expecting 'profile'
                    _uiState.update { state ->
                        state.copy(
                            organizationName = profile.organizationName,
                            email = profile.email,
                            avatarUrl = profile.avatarUrl
                        )
                    }
                }.onFailure { exception ->
                    // Handle error state here
                }
            }
        }
    }

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