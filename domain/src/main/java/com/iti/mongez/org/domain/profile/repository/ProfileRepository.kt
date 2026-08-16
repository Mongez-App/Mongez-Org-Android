package com.iti.mongez.org.domain.profile.repository

import com.iti.mongez.org.domain.organization.model.Organization
import com.iti.mongez.org.domain.profile.model.Profile
import kotlinx.coroutines.flow.Flow

    interface ProfileRepository {
        fun getProfile(): Flow<Result<Profile>>
    }