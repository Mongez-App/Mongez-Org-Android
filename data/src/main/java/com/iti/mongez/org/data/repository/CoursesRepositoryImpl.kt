package com.iti.mongez.org.data.repository

import com.iti.mongez.org.data.remote.api.CoursesApi
import com.iti.mongez.org.data.remote.dto.CreateCourseRequestDto
import com.iti.mongez.org.data.utils.network.safeApi
import com.iti.mongez.org.domain.core.Alert
import com.iti.mongez.org.domain.core.Result
import com.iti.mongez.org.domain.courses.model.*
import com.iti.mongez.org.domain.courses.repository.CoursesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CoursesRepositoryImpl @Inject constructor(
    private val coursesApi: CoursesApi
) : CoursesRepository {

    override suspend fun getCourses(teamId: String): Result<List<Course>> {
        return safeApi {
            coursesApi.getCourses(teamId).courses.map { it.toDomain() }
        }
    }

    override suspend fun createCourse(
        teamId: String,
        name: String,
        startDate: String,
        endDate: String,
        thumbnailUrl: String,
        materialIds: List<String>
    ): Result<CourseCreationResult> {
        return safeApi {
            val request = CreateCourseRequestDto(
                teamId = teamId,
                name = name,
                startDate = startDate,
                endDate = endDate,
                thumbnailUrl = thumbnailUrl,
                materialIds = materialIds
            )
            val response = coursesApi.createCourse(request)
            CourseCreationResult(
                course = response.toDomain(),
                alertMessage = "Course created successfully"
            )
        }
    }

    override suspend fun getCourseDetails(courseId: String): Result<Course> {
        return safeApi {
            coursesApi.getCourseDetails(courseId).data?.toDomain() ?: throw Exception("Not found")
        }
    }

    override suspend fun getCourseTasks(courseId: String): Result<List<CourseTask>> {
        return safeApi {
            coursesApi.getCourseTasks(courseId).data?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun updateCourse(
        courseId: String,
        name: String,
        imageUrl: String,
        isHidden: Boolean
    ): Result<CourseActionResponse<Course>> {
        return safeApi {
            val body = mapOf(
                "name" to name,
                "thumbnailUrl" to imageUrl,
                "is_hidden" to isHidden
            )
            val response = coursesApi.updateCourse(courseId, body)
            CourseActionResponse(
                data = response.data?.toDomain() ?: throw Exception("Update failed"),
                alert = Alert(response.message)
            )
        }
    }

    override suspend fun deleteCourse(courseId: String): Result<CourseActionResponse<Unit>> {
        return safeApi {
            val response = coursesApi.deleteCourse(courseId)
            CourseActionResponse(data = Unit, alert = Alert(response.message))
        }
    }

    override suspend fun getCourseMaterials(courseId: String): Result<List<CourseMaterial>> {
        return safeApi {
            coursesApi.getCourseMaterials(courseId).data?.map { it.toDomain() } ?: emptyList()
        }
    }

    override suspend fun uploadCourseMaterial(
        courseId: String,
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray,
        deviceFileUri: String?
    ): Result<CourseActionResponse<Unit>> {
        return safeApi {
            val filePart = MultipartBody.Part.createFormData(
                "file",
                fileName,
                fileBytes.toRequestBody(contentType.toMediaTypeOrNull())
            )
            val namePart = fileName.toRequestBody("text/plain".toMediaTypeOrNull())
            val pageCountPart = pageCount.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val response = coursesApi.uploadCourseMaterial(courseId, filePart, namePart, pageCountPart)
            CourseActionResponse(data = Unit, alert = Alert(response.message))
        }
    }

    override suspend fun deleteCourseMaterial(
        courseId: String,
        materialId: String
    ): Result<CourseActionResponse<Unit>> {
        return safeApi {
            val response = coursesApi.deleteCourseMaterial(courseId, materialId)
            CourseActionResponse(data = Unit, alert = Alert(response.message))
        }
    }
}
