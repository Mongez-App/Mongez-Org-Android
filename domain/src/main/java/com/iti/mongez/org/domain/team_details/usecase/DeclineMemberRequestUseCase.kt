package com.iti.mongez.org.domain.team_details.usecase

import com.iti.mongez.org.domain.team_details.repository.TeamDetailsRepository
import javax.inject.Inject

class DeclineMemberRequestUseCase @Inject constructor(
    private val repository: TeamDetailsRepository
) {
    suspend operator fun invoke(memberId: String) = repository.declineMemberRequest(memberId)
}
