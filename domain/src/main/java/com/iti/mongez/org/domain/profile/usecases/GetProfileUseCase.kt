package com.iti.mongez.org.domain.profile.usecases

import com.iti.mongez.org.domain.organization.model.Organization
import com.iti.mongez.org.domain.profile.model.Profile
import com.iti.mongez.org.domain.profile.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    // Change Organization to Profile here
    operator fun invoke(): Flow<Result<Profile>> {
        return profileRepository.getProfile()
    }
}