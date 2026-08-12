package com.iti.mongez.org.domain.auth.model

data class RegistrationProgress(
    val token: String?,
    val currentStep: Int
)
