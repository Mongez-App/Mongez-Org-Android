package com.iti.mongez.org.presentation.auth.register.uiState

sealed interface RegisterEffect {
    data object NavigateToHome : RegisterEffect
    data object NavigateToLogin : RegisterEffect
    data class NavigateToStep(val step: Int) : RegisterEffect
    data class ShowError(val message: String) : RegisterEffect
}
