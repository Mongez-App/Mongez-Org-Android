package com.iti.mongez.org.domain.teams.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class CreateTeamEventUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    suspend operator fun invoke(
        teamId: String,
        courseId: String,
        eventType: String,
        eventDate: String
    ) = repository.createTeamEvent(teamId, courseId, eventType, eventDate)
}
