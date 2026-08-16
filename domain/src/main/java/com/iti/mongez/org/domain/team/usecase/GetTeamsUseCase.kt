package com.iti.mongez.org.domain.team.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team.model.Team
import com.iti.mongez.org.domain.team.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTeamsUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    operator fun invoke(): Flow<Result<List<Team>>> = repository.getTeams()
}
