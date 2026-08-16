package com.iti.mongez.org.domain.courses.usecase

import com.iti.mongez.org.domain.courses.model.CourseActionResponse
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import javax.inject.Inject

class UploadCourseMaterialUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) {
    suspend operator fun invoke(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray,
        deviceFileUri: String? = null
    ): Result<CourseActionResponse<Unit>> {
        return coursesRepository.uploadCourseMaterial(courseId, fileName, contentType, fileSizeBytes, pageCount, fileBytes,deviceFileUri)
    }
}