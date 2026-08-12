package com.iti.mongez.org.presentation.auth.register.contract

sealed interface RegisterIntent {
    // Step 1: Credentials
    data class OnEmailChanged(val email: String) : RegisterIntent
    data class OnPasswordChanged(val password: String) : RegisterIntent
    data class OnConfirmPasswordChanged(val confirmPassword: String) : RegisterIntent
    data object OnStep1Next : RegisterIntent
    
    // Step 2: Basic Info
    data class OnOrgNameChanged(val name: String) : RegisterIntent
    data class OnIndustryFieldChanged(val field: String) : RegisterIntent
    data class OnDescriptionChanged(val description: String) : RegisterIntent
    data class OnLogoSelected(val uri: String?) : RegisterIntent
    data object OnStep2Next : RegisterIntent
    data object OnStep2Back : RegisterIntent
    
    // Step 3: Services & Audience
    data class OnTargetAudienceChanged(val audience: List<String>) : RegisterIntent
    data class OnServicesChanged(val services: List<String>) : RegisterIntent
    data class OnMembersCountChanged(val count: String) : RegisterIntent
    data object OnStep3Next : RegisterIntent
    data object OnStep3Back : RegisterIntent
    
    // Step 4: Contact & Location
    data class OnContactEmailChanged(val email: String) : RegisterIntent
    data class OnPhoneNumberChanged(val phone: String) : RegisterIntent
    data class OnWebsiteUrlChanged(val url: String) : RegisterIntent
    data class OnAddressChanged(val address: String) : RegisterIntent
    data class OnLocationChanged(val lat: Double, val lng: Double) : RegisterIntent
    data class OnRegistrationNumberChanged(val number: String) : RegisterIntent
    data class OnDocumentsChanged(val documents: List<String>) : RegisterIntent
    data object OnNavigateToLocationPicker : RegisterIntent
    data object OnStep4Submit : RegisterIntent
    data object OnStep4Back : RegisterIntent
    
    // Step 5: Review
    data object OnNavigateToHome : RegisterIntent
    data object OnBackToLogin : RegisterIntent
}
