package com.iti.mongez.org.domain.team_details.usecase

import com.iti.mongez.org.domain.team_details.repository.TeamDetailsRepository
import javax.inject.Inject

class GetTeamEventsUseCase @Inject constructor(
    private val repository: TeamDetailsRepository
) {
    suspend operator fun invoke(teamId: String) = repository.getTeamEvents(teamId)
}
