package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.remote.api.TeamsApi
import com.iti.mongez.org.data.remote.models.team.CreateTeamRequest
import com.iti.mongez.org.data.remote.models.team.UploadPhotoRequest
import com.iti.mongez.org.data.utils.network.safeApi
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team.model.Event
import com.iti.mongez.org.domain.team.model.Team
import com.iti.mongez.org.domain.team.repository.TeamManagementFlowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TeamManagementFlowRepositoryImpl @Inject constructor(
    private val teamsApi: TeamsApi
) : TeamManagementFlowRepository {

    override fun getTeams(): Flow<Result<List<Team>>> = flow {
        emit(Result.Loading)
        val result = safeApi {
            val response = teamsApi.getTeams()
            response.teams?.map { dto ->
                Team(
                    id = dto.id.orEmpty(),
                    name = dto.name.orEmpty(),
                    photoUrl = dto.photoUrl.orEmpty(),
                    ownerId = dto.ownerId,
                    memberCount = dto.memberCount ?: 0,
                    progress = dto.progress ?: 0,
                    events = dto.events?.map { eventDto ->
                        Event(
                            id = eventDto.id.orEmpty(),
                            name = eventDto.name.orEmpty()
                        )
                    } ?: emptyList(),
                    createdAt = dto.createdAt,
                    updatedAt = dto.updatedAt
                )
            } ?: emptyList()
        }
        emit(result)
    }

    override fun createTeam(name: String, photoUrl: String, inviteCode: String): Flow<Result<Team>> = flow {
        emit(Result.Loading)
        val result = safeApi {
            val response = teamsApi.createTeam(
                CreateTeamRequest(
                    name = name,
                    photoUrl = photoUrl,
                    inviteCode = inviteCode
                )
            )
            Team(
                id = response.id.orEmpty(),
                name = response.name.orEmpty(),
                photoUrl = response.photoUrl.orEmpty(),
                ownerId = response.ownerId,
                memberCount = response.memberCount ?: 0,
                progress = response.progress ?: 0,
                events = response.events?.map { eventDto ->
                    Event(
                        id = eventDto.id.orEmpty(),
                        name = eventDto.name.orEmpty()
                    )
                } ?: emptyList(),
                createdAt = response.createdAt,
                updatedAt = response.updatedAt
            )
        }
        emit(result)
    }

    override fun uploadTeamPhoto(fileUrl: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        val result = safeApi {
            val response = teamsApi.uploadTeamPhoto(UploadPhotoRequest(fileUrl = fileUrl))
            response.fileUrl ?: fileUrl // fallback to the input fileUrl if null
        }
        emit(result)
    }
}
