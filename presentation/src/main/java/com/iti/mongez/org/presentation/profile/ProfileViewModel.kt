package com.iti.mongez.org.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.profile.usecases.GetProfileUseCase
import com.iti.mongez.org.domain.settings.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        settingsRepository.isDarkMode
            .onEach { isDark ->
                _uiState.update { it.copy(isDarkMode = isDark ?: false) }
            }
            .launchIn(viewModelScope)

        settingsRepository.language
            .onEach { lang ->
                _uiState.update { it.copy(currentLanguage = lang.uppercase()) }
            }
            .launchIn(viewModelScope)
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.FetchProfileData -> fetchProfileData()
            is ProfileIntent.ToggleDarkMode -> onDarkModeChanged(intent.enabled)
            is ProfileIntent.SelectLanguage -> onLanguageSelected(intent.language)
            is ProfileIntent.ToggleAvatarDialog -> onAvatarClicked(intent.show)
            is ProfileIntent.SelectAvatar -> onAvatarSelected(intent.url)
            is ProfileIntent.RemoveAvatar -> onRemoveAvatar()
            is ProfileIntent.ToggleLogoutDialog -> onLogoutClicked(intent.show)
            is ProfileIntent.ConfirmLogout -> {
                viewModelScope.launch {
                    // TODO: Implement actual logout logic (clear session, etc.)
                    onLogoutClicked(false)
                    _effect.emit(ProfileEffect.NavigateToLogin)
                }
            }
        }
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
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
        }
    }

    fun onLanguageSelected(language: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(language)
        }
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