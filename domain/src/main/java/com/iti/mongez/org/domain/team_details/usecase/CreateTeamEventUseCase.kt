package com.iti.mongez.org.domain.team_details.usecase

import com.iti.mongez.org.domain.team_details.repository.TeamDetailsRepository
import javax.inject.Inject

class CreateTeamEventUseCase @Inject constructor(
    private val repository: TeamDetailsRepository
) {
    suspend operator fun invoke(
        teamId: String,
        courseId: String,
        eventType: String,
        eventDate: String
    ) = repository.createTeamEvent(teamId, courseId, eventType, eventDate)
}
