package com.iti.mongez.org.data.remote.api

import com.iti.mongez.org.data.remote.models.team.CreateTeamRequest
import com.iti.mongez.org.data.remote.models.team.GetTeamsResponse
import com.iti.mongez.org.data.remote.models.team.TeamDto
import com.iti.mongez.org.data.remote.models.team.UploadPhotoRequest
import com.iti.mongez.org.data.remote.models.team.UploadPhotoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TeamsApi {
    @GET("getTeams")
    suspend fun getTeams(): GetTeamsResponse

    @POST("createTeam")
    suspend fun createTeam(@Body request: CreateTeamRequest): TeamDto

    @POST("uploadTeamPhoto")
    suspend fun uploadTeamPhoto(@Body request: UploadPhotoRequest): UploadPhotoResponse
}
