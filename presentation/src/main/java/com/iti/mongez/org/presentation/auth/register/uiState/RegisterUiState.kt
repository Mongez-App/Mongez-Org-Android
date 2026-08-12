package com.iti.mongez.org.presentation.auth.register.uiState

data class RegisterUiState(
    val currentStep: Int = 1,
    val isLoading: Boolean = false,
    
    // Step 1
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    
    // Step 2
    val orgName: String = "",
    val industryField: String = "",
    val description: String = "",
    val logoUri: String? = null,
    val orgNameError: String? = null,
    val industryFieldError: String? = null,
    
    // Step 3
    val targetAudience: List<String> = listOf(""),
    val services: List<String> = listOf(""),
    val membersCount: String = "",
    val membersCountError: String? = null,
    
    // Step 4
    val contactEmail: String = "",
    val phoneNumber: String = "",
    val websiteUrl: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val registrationNumber: String = "",
    val documents: List<String> = emptyList(),
    val contactEmailError: String? = null,
    val phoneNumberError: String? = null,
    
    // Step 5
    val isReviewComplete: Boolean = false,
    val isAccepted: Boolean = false
)
