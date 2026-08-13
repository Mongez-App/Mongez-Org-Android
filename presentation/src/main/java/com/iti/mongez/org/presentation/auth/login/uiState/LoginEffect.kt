package com.iti.mongez.org.presentation.auth.login.uiState

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect
    data object NavigateToSignUp : LoginEffect
    data object LaunchGoogleSignIn : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
