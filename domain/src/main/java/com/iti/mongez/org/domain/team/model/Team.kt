package com.iti.mongez.org.domain.team.model

data class Team(
    val id: String,
    val name: String,
    val photoUrl: String,
    val memberCount: Int,
    val progress: Int,
    val events: List<Event> = emptyList(),
    val ownerId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
