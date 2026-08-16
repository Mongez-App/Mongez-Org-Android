package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.remote.api.ProfileApi
import com.iti.mongez.org.domain.profile.repository.ProfileRepository
import com.iti.mongez.org.domain.profile.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
) : ProfileRepository {

    override fun getProfile(): Flow<Result<Profile>> = flow {
        try {
            val response = profileApi.getProfile()

            if (response.isSuccessful) {
                // Now returning the dedicated ProfileDto
                val dto = response.body()

                if (dto != null) {
                    // Map the ProfileDto fields to the domain Profile model
                    val profile = Profile(
                        id = dto.id,
                        organizationName = dto.name,
                        email = dto.email,
                        avatarUrl = dto.photoUrl
                    )

                    emit(Result.success(profile))
                } else {
                    emit(Result.failure(Exception("Empty response body")))
                }
            } else {
                emit(Result.failure(Exception(response.message() ?: "An unknown error occurred")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}