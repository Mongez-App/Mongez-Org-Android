package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.model.RegistrationProgress
import com.iti.mongez.org.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetRegistrationProgressUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): RegistrationProgress {
        return RegistrationProgress(
            token = authRepository.getAuthToken(),
            currentStep = authRepository.getRegistrationStep()
        )
    }
}
