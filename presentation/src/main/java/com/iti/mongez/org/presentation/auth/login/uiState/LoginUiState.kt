package com.iti.mongez.org.presentation.auth.login.uiState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)
