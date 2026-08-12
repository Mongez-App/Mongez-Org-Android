package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.repository.AuthRepository
import com.iti.mongez.org.domain.core.Result
import javax.inject.Inject

class LogoutOrganizationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepository.logoutOrganization()
    }
}

