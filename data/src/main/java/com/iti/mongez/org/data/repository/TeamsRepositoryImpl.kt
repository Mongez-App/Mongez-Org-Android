package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.remote.api.CoursesApi
import com.iti.mongez.org.data.remote.dto.CreateEventRequestDto
import com.iti.mongez.org.data.utils.network.safeApi
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.teams.model.TeamEvent
import com.iti.mongez.org.domain.teams.repository.TeamsRepository
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val api: CoursesApi
) : TeamsRepository {

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
}
