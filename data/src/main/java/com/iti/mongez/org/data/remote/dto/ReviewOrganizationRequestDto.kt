package com.iti.mongez.org.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iti.mongez.org.domain.auth.model.ReviewOrganizationRequest

data class ReviewOrganizationRequestDto(
    @SerializedName("org_name") val orgName: String,
    @SerializedName("desc") val description: String,
    @SerializedName("org_field") val orgField: String,
    @SerializedName("target_audience") val targetAudience: List<String>,
    @SerializedName("service") val services: List<String>,
    @SerializedName("members") val members: Int,
    @SerializedName("contact_email") val contactEmail: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("website_url") val websiteUrl: String?,
    @SerializedName("address") val address: String,
    @SerializedName("location") val location: LocationDto,
    @SerializedName("logo_url") val logoUrl: String?,
    @SerializedName("registration_number") val registrationNumber: String?,
    @SerializedName("documents") val documents: List<String>?
) {
    companion object {
        fun fromDomain(request: ReviewOrganizationRequest) = ReviewOrganizationRequestDto(
            orgName = request.orgName,
            description = request.description,
            orgField = request.orgField,
            targetAudience = request.targetAudience,
            services = request.services,
            members = request.members,
            contactEmail = request.contactEmail,
            phoneNumber = request.phoneNumber,
            websiteUrl = request.websiteUrl,
            address = request.address,
            location = LocationDto(lat = request.latitude, lng = request.longitude),
            logoUrl = request.logoUri,
            registrationNumber = request.registrationNumber,
            documents = request.documents
        )
    }
}

data class LocationDto(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)
