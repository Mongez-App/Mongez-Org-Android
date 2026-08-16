package com.iti.mongez.org.domain.team.repository

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {
    fun getTeams(): Flow<Result<List<Team>>>
    fun createTeam(name: String, photoUrl: String, inviteCode: String): Flow<Result<Team>>
    fun uploadTeamPhoto(fileUrl: String): Flow<Result<String>> // Returns uploaded fileUrl
}
