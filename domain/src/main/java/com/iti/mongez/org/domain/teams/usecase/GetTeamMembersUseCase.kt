package com.iti.mongez.org.domain.teams.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class GetTeamMembersUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    suspend operator fun invoke(teamId: String) = repository.getTeamMembers(teamId)
}
