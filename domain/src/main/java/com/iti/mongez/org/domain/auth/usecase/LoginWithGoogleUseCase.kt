package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.repository.AuthRepository
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.organization.model.Organization
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<Organization> {
        return authRepository.loginWithGoogle(idToken)
    }
}
