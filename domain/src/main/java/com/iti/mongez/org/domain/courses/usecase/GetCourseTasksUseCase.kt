package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.core.Result

import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import com.iti.mongez.org.domain.courses.model.CourseTask
import javax.inject.Inject

class GetCourseTasksUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String): Result<List<CourseTask>> {
        return repository.getCourseTasks(courseId)
    }
}
