package com.iti.mongez.org.presentation.auth.login.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.org.domain.auth.usecase.LoginOrganizationUseCase
import com.iti.mongez.org.domain.auth.usecase.LoginWithGoogleUseCase
import com.iti.mongez.org.domain.core.exception.AppException
import com.iti.mongez.org.presentation.auth.login.contract.LoginIntent
import com.iti.mongez.org.presentation.auth.login.uiState.LoginEffect
import com.iti.mongez.org.presentation.auth.login.uiState.LoginUiState
import com.iti.mongez.org.presentation.utils.toFriendlyMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginOrganizationUseCase: LoginOrganizationUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnEmailChanged -> {
                _uiState.update { it.copy(email = intent.email, emailError = null) }
            }
            is LoginIntent.OnPasswordChanged -> {
                _uiState.update { it.copy(password = intent.password, passwordError = null) }
            }
            LoginIntent.OnLoginClicked -> {
                login()
            }
            LoginIntent.OnGoogleSignInClicked -> {
                viewModelScope.launch {
                    _effect.emit(LoginEffect.LaunchGoogleSignIn)
                }
            }
            is LoginIntent.OnGoogleIdTokenReceived -> {
                loginWithGoogle(intent.idToken)
            }
            LoginIntent.OnSignUpClicked -> {
                viewModelScope.launch {
                    _effect.emit(LoginEffect.NavigateToSignUp)
                }
            }
            LoginIntent.OnForgotPasswordClicked -> {
                // TODO: handle forgot password
            }
        }
    }

    private fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password
        
        var hasError = false
        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email cannot be empty") }
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(emailError = "Invalid email format") }
            hasError = true
        }

        if (password.isBlank()) {
            _uiState.update { it.copy(passwordError = "Password cannot be empty") }
            hasError = true
        }
        
        if (hasError) return
        
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = loginOrganizationUseCase(email, password)
            _uiState.update { it.copy(isLoading = false) }
            
            result.fold(
                onSuccess = {
                    _effect.emit(LoginEffect.NavigateToHome)
                },
                onFailure = { error ->
                    val message = (error as? AppException)?.toFriendlyMessage() ?: error.message ?: "Unknown Error"
                    _effect.emit(LoginEffect.ShowError(message))
                },
                onLoading = {}
            )
        }
    }

    private fun loginWithGoogle(idToken: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = loginWithGoogleUseCase(idToken)
            _uiState.update { it.copy(isLoading = false) }
            
            result.fold(
                onSuccess = {
                    _effect.emit(LoginEffect.NavigateToSignUpStep2)
                },
                onFailure = { error ->
                    val message = (error as? AppException)?.toFriendlyMessage() ?: error.message ?: "Unknown Error"
                    _effect.emit(LoginEffect.ShowError(message))
                },
                onLoading = {}
            )
        }
    }
}
