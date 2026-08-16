package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.remote.api.CoursesApi
import com.iti.mongez.org.data.remote.dto.CreateEventRequestDto
import com.iti.mongez.org.data.remote.dto.MemberActionRequestDto
import com.iti.mongez.org.data.utils.network.safeApi
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team_details.model.TeamEvent
import com.iti.mongez.org.domain.team_details.model.TeamMembers
import com.iti.mongez.org.domain.team_details.repository.TeamDetailsRepository
import javax.inject.Inject

class TeamDetailsRepositoryImpl @Inject constructor(
    private val api: CoursesApi
) : TeamDetailsRepository {

    override suspend fun getTeamEvents(teamId: String): Result<List<TeamEvent>> {
        return safeApi {
            api.getEvents(teamId).events.map { it.toDomain() }
        }
    }

    override suspend fun createTeamEvent(
        teamId: String,
        courseId: String,
        eventType: String,
        eventDate: String
    ): Result<TeamEvent> {
        return safeApi {
            val request = CreateEventRequestDto(
                teamId = teamId,
                courseId = courseId,
                eventType = eventType,
                eventDate = eventDate
            )
            api.createEvent(request).toDomain()
        }
    }

    override suspend fun getTeamMembers(teamId: String): Result<TeamMembers> {
        return safeApi {
            api.getMembers(teamId).toDomain()
        }
    }

    override suspend fun acceptMemberRequest(memberId: String): Result<Unit> {
        return safeApi {
            api.acceptMember(MemberActionRequestDto(memberId))
            Unit
        }
    }

    override suspend fun declineMemberRequest(memberId: String): Result<Unit> {
        return safeApi {
            api.declineMember(MemberActionRequestDto(memberId))
        }
    }
}
