package com.iti.mongez.org.domain.courses.repository

import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.courses.model.*

interface CoursesRepository {
    suspend fun getCourses(teamId: String): Result<List<Course>>
    suspend fun createCourse(
        teamId: String,
        name: String,
        startDate: String,
        endDate: String,
        thumbnailUrl: String,
        materialIds: List<String>
    ): Result<CourseCreationResult>
    suspend fun getCourseDetails(courseId: String): Result<Course>
    suspend fun getCourseTasks(courseId: String): Result<List<CourseTask>>
    suspend fun updateCourse(courseId: String, name: String, imageUrl: String, isHidden: Boolean): Result<CourseActionResponse<Course>>    suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>>
    
    suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>>
    suspend fun uploadCourseMaterial(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray,
        deviceFileUri: String?
    ): Result<CourseActionResponse<Unit>>    suspend fun deleteCourseMaterial(courseId: String, materialId: String): Result<CourseActionResponse<Unit>>
}