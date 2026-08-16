package com.iti.mongez.org.data.remote.api

import com.iti.mongez.org.data.remote.dto.ProfileDto
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("getProfile")
    suspend fun getProfile(): Response<ProfileDto>
}