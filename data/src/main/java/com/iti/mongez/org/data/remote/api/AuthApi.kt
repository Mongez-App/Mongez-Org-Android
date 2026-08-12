package com.iti.mongez.org.data.remote.api

import com.iti.mongez.org.data.remote.dto.AuthResponseDto
import com.iti.mongez.org.data.remote.dto.RegisterRequestDto
import com.iti.mongez.org.data.remote.dto.ReviewOrganizationRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("organizations/auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("organizations/auth/login")
    suspend fun login(): AuthResponseDto

    @POST("organizations/auth/logout")
    suspend fun logout()

    @POST("organizations/review_organization")
    suspend fun reviewOrganization(@Body request: ReviewOrganizationRequestDto)
}
