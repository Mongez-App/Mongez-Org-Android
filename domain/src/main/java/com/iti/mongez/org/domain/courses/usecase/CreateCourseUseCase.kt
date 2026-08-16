package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.core.Result

import com.iti.mongez.org.domain.courses.model.CourseCreationResult
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class CreateCourseUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(
        teamId: String,
        name: String,
        startDate: String,
        endDate: String,
        thumbnailUrl: String,
        materialIds: List<String>
    ): Result<CourseCreationResult> {
        return repository.createCourse(
            teamId = teamId,
            name = name,
            startDate = startDate,
            endDate = endDate,
            thumbnailUrl = thumbnailUrl,
            materialIds = materialIds
        )
    }
}