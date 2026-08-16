package com.iti.mongez.org.data.remote.api

import com.iti.mongez.org.data.remote.dto.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface CoursesApi {

    @GET("getCourses")
    suspend fun getCourses(@Query("teamId") teamId: String): GetCoursesResponseDto

    @POST("createCourse")
    suspend fun createCourse(@Body request: CreateCourseRequestDto): CourseDto

    @GET("courses/{courseId}")
    suspend fun getCourseDetails(@Path("courseId") courseId: String): BaseResponseDto<CourseDto>

    @GET("courses/{courseId}/tasks")
    suspend fun getCourseTasks(@Path("courseId") courseId: String): BaseResponseDto<List<CourseTaskDto>>

    @PATCH("courses/{courseId}")
    suspend fun updateCourse(
        @Path("courseId") courseId: String,
        @Body request: Map<String, Any>
    ): BaseResponseDto<CourseDto>

    @DELETE("courses/{courseId}")
    suspend fun deleteCourse(@Path("courseId") courseId: String): BaseResponseDto<Unit>

    @GET("courses/{courseId}/materials")
    suspend fun getCourseMaterials(@Path("courseId") courseId: String): BaseResponseDto<List<CourseMaterialDto>>

    @Multipart
    @POST("courses/{courseId}/materials")
    suspend fun uploadCourseMaterial(
        @Path("courseId") courseId: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("page_count") pageCount: RequestBody
    ): BaseResponseDto<Unit>

    @DELETE("courses/{courseId}/materials/{materialId}")
    suspend fun deleteCourseMaterial(
        @Path("courseId") courseId: String,
        @Path("materialId") materialId: String
    ): BaseResponseDto<Unit>

    @GET("getEvents")
    suspend fun getEvents(@Query("teamId") teamId: String): GetEventsResponseDto

    @POST("createEvent")
    suspend fun createEvent(@Body request: CreateEventRequestDto): EventDto
}

// Adding BaseResponseDto if it doesn't exist or just using a generic one
data class BaseResponseDto<T>(
    val message: String,
    val data: T?
)
