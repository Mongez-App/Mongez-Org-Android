package com.iti.mongez.org.domain.auth.usecase

import com.iti.mongez.org.domain.auth.model.ReviewOrganizationRequest
import com.iti.mongez.org.domain.auth.repository.AuthRepository
import com.iti.mongez.org.domain.core.Result
import javax.inject.Inject

class SubmitOrganizationReviewUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(request: ReviewOrganizationRequest): Result<Unit> {
        return authRepository.submitOrganizationReview(request)
    }
}
