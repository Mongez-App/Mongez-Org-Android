package com.iti.mongez.org.data.remote.models.team

import com.google.gson.annotations.SerializedName

data class GetTeamsResponse(
    @SerializedName("teams")
    val teams: List<TeamDto>?,
    @SerializedName("total")
    val total: Int?
)

data class TeamDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("photoUrl")
    val photoUrl: String?,
    @SerializedName("ownerId")
    val ownerId: String?,
    @SerializedName("memberCount")
    val memberCount: Int?,
    @SerializedName("progress")
    val progress: Int?,
    @SerializedName("events")
    val events: List<EventDto>?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?
)

data class EventDto(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)

data class CreateTeamRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("photoUrl")
    val photoUrl: String,
    @SerializedName("invite_code")
    val inviteCode: String
)

data class UploadPhotoRequest(
    @SerializedName("fileUrl")
    val fileUrl: String
)

data class UploadPhotoResponse(
    @SerializedName("fileUrl")
    val fileUrl: String?
)
