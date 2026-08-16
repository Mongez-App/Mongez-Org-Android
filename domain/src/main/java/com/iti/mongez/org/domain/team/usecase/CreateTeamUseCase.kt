package com.iti.mongez.org.domain.team.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team.model.Team
import com.iti.mongez.org.domain.team.repository.TeamManagementFlowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateTeamUseCase @Inject constructor(
    private val repository: TeamManagementFlowRepository
) {
    operator fun invoke(name: String, photoUrl: String, inviteCode: String): Flow<Result<Team>> {
        return repository.createTeam(name, photoUrl, inviteCode)
    }
}
