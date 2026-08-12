package com.iti.mongez.org.domain.auth.repository

import com.iti.mongez.org.domain.auth.model.ReviewOrganizationRequest
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.organization.model.Organization

interface AuthRepository {
    suspend fun registerOrganization(name: String, email: String, password: String): Result<Organization>
    suspend fun loginOrganization(email: String, password: String): Result<Organization>
    suspend fun loginWithGoogle(idToken: String): Result<Organization>
    suspend fun logoutOrganization(): Result<Unit>
    suspend fun submitOrganizationReview(request: ReviewOrganizationRequest): Result<Unit>
    suspend fun checkAuthenticationState(): Result<Boolean>
    suspend fun verifyTokenWithBackend(): Result<Unit>
    suspend fun saveRegistrationStep(step: Int)
    suspend fun getRegistrationStep(): Int
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
    suspend fun saveDraft(draft: String)
    suspend fun getDraft(): String?
    suspend fun clearAuthData()
}
