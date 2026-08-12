package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SaveRegistrationProgressUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(step: Int) {
        authRepository.saveRegistrationStep(step)
    }
}
