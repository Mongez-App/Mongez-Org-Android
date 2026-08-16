package com.iti.mongez.org.data.remote.api

import com.iti.mongez.org.data.remote.dto.AuthResponseDto
import com.iti.mongez.org.data.remote.dto.OrganizationDto
import com.iti.mongez.org.data.remote.dto.ProfileDto
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("organization/getProfile")
    suspend fun getProfile(): Response<ProfileDto>
}