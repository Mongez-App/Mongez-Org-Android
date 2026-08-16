package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.courses.model.CourseMaterial
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class GetCourseMaterialsUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(courseId: String): Result<List<CourseMaterial>> {
        return coursesRepository.getCourseMaterials(courseId)
    }
}
