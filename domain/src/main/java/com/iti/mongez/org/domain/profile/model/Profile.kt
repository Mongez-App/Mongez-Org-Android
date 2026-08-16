package com.iti.mongez.org.domain.profile.model

data class Profile(
    val id: String,
    val organizationName: String,
    val email: String,
    val avatarUrl: String?
)