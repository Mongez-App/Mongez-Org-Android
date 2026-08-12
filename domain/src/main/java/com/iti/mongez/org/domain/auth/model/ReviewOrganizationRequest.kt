package com.iti.mongez.org.domain.auth.model

data class ReviewOrganizationRequest(
    val orgName: String,
    val description: String,
    val orgField: String,
    val targetAudience: List<String>,
    val services: List<String>,
    val members: Int,
    val contactEmail: String,
    val phoneNumber: String,
    val websiteUrl: String?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val logoUri: String?,
    val registrationNumber: String?,
    val documents: List<String>?
)
