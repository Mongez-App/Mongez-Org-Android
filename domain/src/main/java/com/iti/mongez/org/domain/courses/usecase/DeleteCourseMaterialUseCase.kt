package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.courses.model.CourseActionResponse
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class DeleteCourseMaterialUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String, materialId: String): Result<CourseActionResponse<Unit>> {
        return coursesRepository.deleteCourseMaterial(courseId, materialId)
    }
}
