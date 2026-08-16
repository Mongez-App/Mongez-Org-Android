package com.iti.mongez.org.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProfileDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("email") val email: String
)