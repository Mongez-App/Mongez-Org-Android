package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke() = repository.getCourses()
}