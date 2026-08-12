package com.iti.mongez.org.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iti.mongez.org.domain.organization.model.Organization

data class OrganizationDto(
    @SerializedName("uid") val uid: String,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("established_at") val establishedAt: String?,
    @SerializedName("no_of_students") val noOfStudents: Int,
    @SerializedName("no_of_courses") val noOfCourses: Int,
    @SerializedName("no_of_teams") val noOfTeams: Int
) {
    fun toDomain() = Organization(
        uid = uid,
        email = email,
        name = name,
        avatar = avatar,
        description = description,
        establishedAt = establishedAt,
        noOfStudents = noOfStudents,
        noOfCourses = noOfCourses,
        noOfTeams = noOfTeams
    )
}

data class AuthResponseDto(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: OrganizationDto?
)
