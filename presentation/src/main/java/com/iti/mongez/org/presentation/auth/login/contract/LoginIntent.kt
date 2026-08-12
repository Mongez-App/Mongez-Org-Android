package com.iti.mongez.org.presentation.auth.login.contract

sealed interface LoginIntent {
    data class OnEmailChanged(val email: String) : LoginIntent
    data class OnPasswordChanged(val password: String) : LoginIntent
    data object OnLoginClicked : LoginIntent
    data object OnGoogleSignInClicked : LoginIntent
    data class OnGoogleIdTokenReceived(val idToken: String) : LoginIntent
    data object OnSignUpClicked : LoginIntent
    data object OnForgotPasswordClicked : LoginIntent
}
