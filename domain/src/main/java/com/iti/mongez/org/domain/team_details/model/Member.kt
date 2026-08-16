package com.iti.mongez.org.domain.team_details.model

data class Member(
    val id: String,
    val name: String,
    val role: String,
    val imageUrl: String? = null
)
