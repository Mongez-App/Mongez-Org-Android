package com.iti.mongez.org.domain.team_details.model

data class Team(
    val id: String,
    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,
    val membersCount: Int = 0
)
