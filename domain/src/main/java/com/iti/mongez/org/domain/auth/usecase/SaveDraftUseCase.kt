package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.repository.AuthRepository
import javax.inject.Inject

class SaveDraftUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(draft: String) {
        authRepository.saveDraft(draft)
    }
}
