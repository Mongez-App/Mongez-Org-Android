package com.iti.mongez.org.presentation.auth.register.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.iti.mongez.org.domain.auth.usecase.GetDraftUseCase
import com.iti.mongez.org.domain.auth.usecase.RegisterOrganizationUseCase
import com.iti.mongez.org.domain.auth.usecase.SaveDraftUseCase
import com.iti.mongez.org.domain.auth.usecase.SaveRegistrationProgressUseCase
import com.iti.mongez.org.domain.core.exception.AppException
import com.iti.mongez.org.domain.core.exception.AuthException
import com.iti.mongez.org.presentation.auth.register.contract.RegisterIntent
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterEffect
import com.iti.mongez.org.presentation.auth.register.uiState.RegisterUiState
import com.iti.mongez.org.presentation.utils.toFriendlyMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class RegisterViewModel @Inject constructor(
    private val registerOrganizationUseCase: RegisterOrganizationUseCase,
    private val getDraftUseCase: GetDraftUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    private val saveRegistrationProgressUseCase: SaveRegistrationProgressUseCase,
    private val gson: Gson
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            val draftJson = getDraftUseCase()
            if (!draftJson.isNullOrEmpty()) {
                try {
                    val draftState = gson.fromJson(draftJson, RegisterUiState::class.java)
                    _uiState.value = draftState
                } catch (e: Exception) {
                    // Ignore parsing errors
                }
            }
            
            // Save draft whenever state changes
            _uiState.collect { state ->
                val json = gson.toJson(state)
                saveDraftUseCase(json)
                saveRegistrationProgressUseCase(state.currentStep)
            }
        }
    }

    fun onIntent(intent: RegisterIntent) {
        when (intent) {
            is RegisterIntent.OnEmailChanged -> _uiState.update { it.copy(email = intent.email, emailError = null) }
            is RegisterIntent.OnPasswordChanged -> _uiState.update { it.copy(password = intent.password, passwordError = null) }
            is RegisterIntent.OnConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = intent.confirmPassword, confirmPasswordError = null) }
            RegisterIntent.OnStep1Next -> handleStep1Next()
            
            is RegisterIntent.OnOrgNameChanged -> _uiState.update { it.copy(orgName = intent.name, orgNameError = null) }
            is RegisterIntent.OnIndustryFieldChanged -> _uiState.update { it.copy(industryField = intent.field, industryFieldError = null) }
            is RegisterIntent.OnDescriptionChanged -> _uiState.update { it.copy(description = intent.description) }
            is RegisterIntent.OnLogoSelected -> _uiState.update { it.copy(logoUri = intent.uri) }
            RegisterIntent.OnStep2Next -> handleStep2Next()
            RegisterIntent.OnStep2Back -> _uiState.update { it.copy(currentStep = 1) }
            
            is RegisterIntent.OnTargetAudienceChanged -> _uiState.update { it.copy(targetAudience = intent.audience) }
            is RegisterIntent.OnServicesChanged -> _uiState.update { it.copy(services = intent.services) }
            is RegisterIntent.OnMembersCountChanged -> _uiState.update { it.copy(membersCount = intent.count, membersCountError = null) }
            RegisterIntent.OnStep3Next -> handleStep3Next()
            RegisterIntent.OnStep3Back -> _uiState.update { it.copy(currentStep = 2) }
            
            is RegisterIntent.OnContactEmailChanged -> _uiState.update { it.copy(contactEmail = intent.email, contactEmailError = null) }
            is RegisterIntent.OnPhoneNumberChanged -> _uiState.update { it.copy(phoneNumber = intent.phone, phoneNumberError = null) }
            is RegisterIntent.OnWebsiteUrlChanged -> _uiState.update { it.copy(websiteUrl = intent.url) }
            is RegisterIntent.OnAddressChanged -> _uiState.update { it.copy(address = intent.address) }
            is RegisterIntent.OnLocationChanged -> _uiState.update { it.copy(latitude = intent.lat, longitude = intent.lng) }
            is RegisterIntent.OnRegistrationNumberChanged -> _uiState.update { it.copy(registrationNumber = intent.number) }
            is RegisterIntent.OnDocumentsChanged -> _uiState.update { it.copy(documents = intent.documents) }
            RegisterIntent.OnStep4Submit -> handleStep4Submit()
            RegisterIntent.OnStep4Back -> _uiState.update { it.copy(currentStep = 3) }
            
            RegisterIntent.OnNavigateToHome -> {
                viewModelScope.launch { _effect.emit(RegisterEffect.NavigateToHome) }
            }
            RegisterIntent.OnBackToLogin -> {
                viewModelScope.launch { _effect.emit(RegisterEffect.NavigateToLogin) }
            }
            RegisterIntent.OnNavigateToLocationPicker -> {
                // Handled in navigation layer
            }
        }
    }

    private fun handleStep1Next() {
        val state = _uiState.value
        var hasError = false
        
        if (state.orgName.isBlank()) {
            _uiState.update { it.copy(orgNameError = "Organization name required") }
            hasError = true
        }
        
        if (state.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email required") }
            hasError = true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            _uiState.update { it.copy(emailError = "Invalid email format") }
            hasError = true
        }

        if (state.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
            hasError = true
        }
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }
            hasError = true
        }
        
        if (hasError) return
        
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = registerOrganizationUseCase(state.orgName, state.email, state.password)
            _uiState.update { it.copy(isLoading = false) }
            
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(currentStep = 2) }
                    _effect.emit(RegisterEffect.NavigateToStep(2))
                },
                onFailure = { error ->
                    val message = (error as? AppException)?.toFriendlyMessage() ?: error.message ?: "Unknown Error"
                    _effect.emit(RegisterEffect.ShowError(message))
                    if (error is AuthException.EmailAlreadyInUse) {
                        _effect.emit(RegisterEffect.NavigateToLogin)
                    } else if (error.message?.contains("already in use", ignoreCase = true) == true) {
                        _effect.emit(RegisterEffect.NavigateToLogin)
                    }
                },
                onLoading = {}
            )
        }
    }

    private fun handleStep2Next() {
        val state = _uiState.value
        var hasError = false
        
        if (state.industryField.isBlank()) {
            _uiState.update { it.copy(industryFieldError = "Industry field required") }
            hasError = true
        }
        
        if (hasError) return
        
        _uiState.update { it.copy(currentStep = 3) }
        viewModelScope.launch { _effect.emit(RegisterEffect.NavigateToStep(3)) }
    }

    private fun handleStep3Next() {
        val state = _uiState.value
        var hasError = false
        
        val count = state.membersCount.toIntOrNull()
        if (count == null || count <= 0) {
            _uiState.update { it.copy(membersCountError = "Valid number > 0 required") }
            hasError = true
        }
        
        if (hasError) return
        
        _uiState.update { it.copy(currentStep = 4) }
        viewModelScope.launch { _effect.emit(RegisterEffect.NavigateToStep(4)) }
    }

    private fun handleStep4Submit() {
        val state = _uiState.value
        var hasError = false
        
        if (state.contactEmail.isBlank()) {
            _uiState.update { it.copy(contactEmailError = "Contact email required") }
            hasError = true
        }
        if (state.phoneNumber.isBlank()) {
            _uiState.update { it.copy(phoneNumberError = "Phone number required") }
            hasError = true
        }
        
        if (hasError) return
        
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // TODO: call submitOrganizationReviewUseCase
            _uiState.update { 
                RegisterUiState(
                    currentStep = 5,
                    isReviewComplete = true
                )
            }
            _effect.emit(RegisterEffect.NavigateToStep(5))
            delay(2000)
            _uiState.update { it.copy(isAccepted = true) }
        }
    }
}
