package com.iti.mongez.org.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.iti.mongez.org.domain.teams.model.TeamEvent

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
