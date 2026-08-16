package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.courses.model.CourseActionResponse
import com.iti.mongez.org.domain.courses.model.Course
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class UpdateCourseUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String, name: String, imageUrl: String, isHidden: Boolean): Result<CourseActionResponse<Course>> {
        return coursesRepository.updateCourse(courseId, name, imageUrl, isHidden)
    }
}