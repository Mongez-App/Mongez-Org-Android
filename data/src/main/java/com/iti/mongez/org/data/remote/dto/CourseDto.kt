package com.iti.mongez.org.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iti.mongez.org.domain.courses.model.Course
import com.iti.mongez.org.domain.courses.model.CourseMaterial
import com.iti.mongez.org.domain.courses.model.CourseTask
import java.time.Instant

data class CourseDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("course_code", alternate = ["courseCode"]) val courseCode: String?,
    @SerializedName("thumbnail_url", alternate = ["thumbnailUrl", "image_url", "imageUrl"]) val thumbnailUrl: String?,
    @SerializedName("start_date", alternate = ["startDate"]) val startDate: String,
    @SerializedName("end_date", alternate = ["endDate", "exam_date", "examDate"]) val endDate: String,
    @SerializedName("has_materials", alternate = ["hasMaterials"]) val hasMaterials: Boolean = false,
    @SerializedName("completion_percentage", alternate = ["completionPercentage"]) val completionPercentage: Float = 0f,
    @SerializedName("is_hidden", alternate = ["isHidden"]) val isHidden: Boolean = false,
    @SerializedName("course_type", alternate = ["courseType"]) val courseType: String = "",
    @SerializedName("material_url", alternate = ["materialUrl"]) val materialUrl: String? = null
) {
    fun toDomain() = Course(
        id = id,
        name = name,
        courseCode = courseCode ?: "",
        imageUrl = thumbnailUrl,
        startDate = startDate,
        examDate = endDate,
        hasMaterials = hasMaterials,
        completionPercentage = completionPercentage,
        isHidden = isHidden,
        courseType = courseType,
        materialUrl = materialUrl
    )
}

data class CourseTaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("priority") val priority: String,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("scheduled_date") val scheduledDate: String
) {
    fun toDomain() = CourseTask(
        id = id,
        title = title,
        durationMinutes = durationMinutes,
        priority = priority,
        isCompleted = isCompleted,
        scheduledDate = scheduledDate
    )
}

data class CourseMaterialDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("page_count") val pageCount: Int,
    @SerializedName("file_size_mb") val fileSizeMb: Double,
    @SerializedName("status") val status: String,
    @SerializedName("uploaded_at") val uploadedAt: String, // Assuming ISO 8601 string from API
    @SerializedName("device_file_uri") val deviceFileUri: String? = null
) {
    fun toDomain() = CourseMaterial(
        id = id,
        name = name,
        pageCount = pageCount,
        fileSizeMb = fileSizeMb,
        status = status,
        uploadedAt = Instant.parse(uploadedAt),
        deviceFileUri = deviceFileUri
    )
}

data class CourseActionResponseDto<T>(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)

data class CreateCourseRequestDto(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("name") val name: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("materialIds") val materialIds: List<String>
)

data class GetCoursesResponseDto(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("courses") val courses: List<CourseDto>,
    @SerializedName("total") val total: Int
)
