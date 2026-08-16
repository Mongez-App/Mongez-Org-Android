package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.courses.model.CourseActionResponse
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String): Result<CourseActionResponse<Unit>> {
        return coursesRepository.deleteCourse(courseId)
    }
}
