package com.iti.mongez.org.domain.team_details.usecase

import com.iti.mongez.org.domain.team_details.repository.TeamDetailsRepository
import javax.inject.Inject

class AcceptMemberRequestUseCase @Inject constructor(
    private val repository: TeamDetailsRepository
) {
    suspend operator fun invoke(memberId: String) = repository.acceptMemberRequest(memberId)
}
