package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.core.Result

import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val repository: CoursesRepository
) {
    suspend operator fun invoke(teamId: String) = repository.getCourses(teamId)
}