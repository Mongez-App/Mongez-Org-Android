package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.local.AuthProgressDataStore
import com.iti.mongez.org.data.remote.api.AuthApi
import com.iti.mongez.org.data.remote.datasource.FirebaseAuthDataSource
import com.iti.mongez.org.data.remote.dto.RegisterRequestDto
import com.iti.mongez.org.data.remote.dto.ReviewOrganizationRequestDto
import com.iti.mongez.org.data.utils.network.safeApi
import com.iti.mongez.org.domain.auth.model.ReviewOrganizationRequest
import com.iti.mongez.org.domain.auth.repository.AuthRepository
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.core.exception.AppException
import com.iti.mongez.org.domain.organization.model.Organization
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val authProgressDataStore: AuthProgressDataStore
) : AuthRepository {

    override suspend fun registerOrganization(name: String, email: String, password: String): Result<Organization> {
        return safeApi {
            // Register with Firebase first and get token
            val token = firebaseAuthDataSource.createOrgWithEmail(email, password)
            android.util.Log.d("AuthRepository", "Firebase Token : $token")
            authProgressDataStore.saveToken(token)
            
            // Then register with backend
            val response = authApi.register(RegisterRequestDto(name = name))
            response.data?.toDomain() ?: throw AppException.UnknownException("Registration returned empty data")
        }
    }

    override suspend fun loginOrganization(email: String, password: String): Result<Organization> {
        return safeApi {
            val token = firebaseAuthDataSource.signInWithEmail(email, password)
            android.util.Log.d("AuthRepository", "Firebase Token : $token")
            authProgressDataStore.saveToken(token)
            
            val response = authApi.login()
            response.data?.toDomain() ?: throw AppException.UnknownException("Login returned empty data")
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<Organization> {
        return safeApi {
            val token = firebaseAuthDataSource.signInWithGoogleCredential(idToken)
            authProgressDataStore.saveToken(token)
            
            val response = authApi.login()
            response.data?.toDomain() ?: throw AppException.UnknownException("Google login returned empty data")
        }
    }

    override suspend fun logoutOrganization(): Result<Unit> {
        return safeApi {
            authApi.logout()
            firebaseAuthDataSource.logout()
            authProgressDataStore.clear()
        }
    }

    override suspend fun submitOrganizationReview(request: ReviewOrganizationRequest): Result<Unit> {
        return safeApi {
            authApi.reviewOrganization(ReviewOrganizationRequestDto.fromDomain(request))
        }
    }

    override suspend fun checkAuthenticationState(): Result<Boolean> {
        return safeApi {
            firebaseAuthDataSource.checkAuthState()
        }
    }

    override suspend fun verifyTokenWithBackend(): Result<Unit> {
        return safeApi {
            authApi.login()
        }
    }

    override suspend fun saveRegistrationStep(step: Int) {
        authProgressDataStore.saveStep(step)
    }

    override suspend fun getRegistrationStep(): Int {
        return authProgressDataStore.getStep()
    }

    override suspend fun saveAuthToken(token: String) {
        authProgressDataStore.saveToken(token)
    }

    override suspend fun getAuthToken(): String? {
        return authProgressDataStore.getToken()
    }

    override suspend fun saveDraft(draft: String) {
        authProgressDataStore.saveDraft(draft)
    }

    override suspend fun getDraft(): String? {
        return authProgressDataStore.getDraft()
    }

    override suspend fun clearAuthData() {
        authProgressDataStore.clear()
    }
}
