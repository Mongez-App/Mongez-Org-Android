package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetDraftUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return authRepository.getDraft()
    }
}
