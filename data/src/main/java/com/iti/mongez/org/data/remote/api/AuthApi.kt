package com.iti.mongez.org.data.remote.api

import com.iti.mongez.org.data.remote.dto.AuthResponseDto
import com.iti.mongez.org.data.remote.dto.RegisterRequestDto
import com.iti.mongez.org.data.remote.dto.ReviewOrganizationRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("organization/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("organization/auth/login")
    suspend fun login(): AuthResponseDto

    @POST("organization/auth/logout")
    suspend fun logout()

    @POST("organization/review_organization")
    suspend fun reviewOrganization(@Body request: ReviewOrganizationRequestDto)
}
