package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.repository.AuthRepository
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.organization.model.Organization
import javax.inject.Inject

class LoginOrganizationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Organization> {
        return authRepository.loginOrganization(email, password)
    }
}
