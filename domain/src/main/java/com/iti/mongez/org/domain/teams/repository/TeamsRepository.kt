package com.iti.mongez.org.domain.teams.repository

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.teams.model.TeamEvent
import com.iti.mongez.org.domain.teams.model.TeamMembers

interface TeamsRepository {
    suspend fun getTeamEvents(teamId: String): Result<List<TeamEvent>>
    suspend fun createTeamEvent(
        teamId: String,
        courseId: String,
        eventType: String,
        eventDate: String
    ): Result<TeamEvent>

    suspend fun getTeamMembers(teamId: String): Result<TeamMembers>
    suspend fun acceptMemberRequest(memberId: String): Result<Unit>
    suspend fun declineMemberRequest(memberId: String): Result<Unit>
}
