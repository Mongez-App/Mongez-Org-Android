package com.iti.mongez.org.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iti.mongez.org.domain.team_details.model.TeamEvent

data class EventDto(
    @SerializedName("id") val id: String,
    @SerializedName("teamId") val teamId: String,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("courseName") val courseName: String?,
    @SerializedName("eventType") val eventType: String,
    @SerializedName("eventDate") val eventDate: String,
    @SerializedName("createdAt") val createdAt: String?
) {
    fun toDomain() = TeamEvent(
        id = id,
        title = eventType,
        date = eventDate,
        location = courseName, // Mapping courseName to location/description context for now
        description = "Course: $courseName"
    )
}

data class GetEventsResponseDto(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("events") val events: List<EventDto>,
    @SerializedName("total") val total: Int
)

data class CreateEventRequestDto(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("courseId") val courseId: String,
    @SerializedName("eventType") val eventType: String,
    @SerializedName("eventDate") val eventDate: String
)

data class MemberDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String?,
    @SerializedName("photoUrl") val imageUrl: String?
) {
    fun toDomain() = com.iti.mongez.org.domain.team_details.model.Member(
        id = id,
        name = name,
        role = role ?: "Member",
        imageUrl = imageUrl
    )
}

data class GetMembersResponseDto(
    @SerializedName("teamId") val teamId: String,
    @SerializedName("pendingMembers") val pendingMembers: List<MemberDto>,
    @SerializedName("teamMembers") val teamMembers: List<MemberDto>,
    @SerializedName("pendingTotal") val pendingTotal: Int,
    @SerializedName("teamTotal") val teamTotal: Int
) {
    fun toDomain() = com.iti.mongez.org.domain.team_details.model.TeamMembers(
        teamId = teamId,
        pendingMembers = pendingMembers.map { it.toDomain() },
        teamMembers = teamMembers.map { it.toDomain() },
        pendingTotal = pendingTotal,
        teamTotal = teamTotal
    )
}

data class MemberActionRequestDto(
    @SerializedName("memberId") val memberId: String
)
