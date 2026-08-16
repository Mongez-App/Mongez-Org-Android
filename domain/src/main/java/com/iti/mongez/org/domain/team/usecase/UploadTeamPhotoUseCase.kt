package com.iti.mongez.org.domain.team.usecase

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.team.repository.TeamsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadTeamPhotoUseCase @Inject constructor(
    private val repository: TeamsRepository
) {
    operator fun invoke(fileUrl: String): Flow<Result<String>> {
        return repository.uploadTeamPhoto(fileUrl)
    }
}
